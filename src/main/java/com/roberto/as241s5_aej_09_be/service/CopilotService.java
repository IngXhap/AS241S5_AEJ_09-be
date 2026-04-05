package com.roberto.as241s5_aej_09_be.service;

import com.roberto.as241s5_aej_09_be.model.QueryRecord;
import com.roberto.as241s5_aej_09_be.repository.QueryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class CopilotService {

    private final WebClient webClient;
    private final QueryRepository repository;

    public CopilotService(@Qualifier("copilotClient") WebClient webClient,
                          QueryRepository repository) {
        this.webClient = webClient;
        this.repository = repository;
    }

    public Mono<String> ask(String prompt) {
        log.info("Enviando consulta a Copilot: {}", prompt);

        Map<String, Object> body = new HashMap<>();
        body.put("message", prompt);
        body.put("conversation_id", null);
        body.put("mode", "CHAT");
        body.put("markdown", true);

        return webClient.post()
                .bodyValue(body)
                .retrieve()
                .onStatus(
                    status -> status.is4xxClientError() || status.is5xxServerError(),
                    response -> response.bodyToMono(String.class)
                        .doOnNext(err -> log.error("Error body Copilot: {}", err))
                        .flatMap(err -> Mono.error(new RuntimeException("Copilot Error: " + err)))
                )
                .bodyToMono(Map.class)
.doOnNext(resp -> log.info("Response completo de Copilot: {}", resp)) // agrega esto
.map(resp -> {
    if (resp.get("message") != null) return (String) resp.get("message");
    if (resp.get("reply") != null)   return (String) resp.get("reply");
    if (resp.get("text") != null)    return (String) resp.get("text");
    // si viene anidado en "data"
    Map<String, Object> data = (Map<String, Object>) resp.get("data");
    if (data != null && data.get("message") != null) return (String) data.get("message");
    return resp.toString();
})
.flatMap(response -> save("copilot", prompt, response))
.doOnSuccess(r -> log.info("Respuesta Copilot guardada correctamente"))
.doOnError(e -> log.error("Error al llamar Copilot: {}", e.getMessage()));
    }

    private Mono<String> save(String source, String prompt, String response) {
        QueryRecord record = QueryRecord.builder()
                .apiSource(source)
                .prompt(prompt)
                .response(response)
                .createdAt(LocalDateTime.now())
                .build();

        return repository.save(record).thenReturn(response);
    }
}
