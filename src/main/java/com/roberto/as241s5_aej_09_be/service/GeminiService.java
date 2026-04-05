package com.roberto.as241s5_aej_09_be.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


import com.roberto.as241s5_aej_09_be.model.QueryRecord;
import com.roberto.as241s5_aej_09_be.repository.QueryRepository;

@Slf4j
@Service
public class GeminiService {

    private final WebClient webClient;
    private final QueryRepository repository;

    // 👇 esto le faltaba
    public GeminiService(@Qualifier("geminiClient") WebClient webClient,
                         QueryRepository repository) {
        this.webClient = webClient;
        this.repository = repository;
    }

    public Mono<String> ask(String prompt) {
        log.info("Enviando consulta a Gemini: {}", prompt);

        Map<String, Object> body = Map.of(
            "contents", List.of(
                Map.of(
                    "role", "user",
                    "parts", List.of(Map.of("text", prompt))
                )
            )
        );

        return webClient.post()
                .bodyValue(body)
                .retrieve()
                .onStatus(
                    status -> status.is4xxClientError() || status.is5xxServerError(),
                    response -> response.bodyToMono(String.class)
                        .doOnNext(err -> log.error("Error body Gemini: {}", err))
                        .flatMap(err -> Mono.error(new RuntimeException("Gemini Error: " + err)))
                )
                .bodyToMono(Map.class)
                .doOnNext(resp -> log.info("Response completo de Gemini: {}", resp))
                .map(resp -> {
                    List<Map> candidates = (List<Map>) resp.get("candidates");
                    Map content = (Map) candidates.get(0).get("content");
                    List<Map> parts = (List<Map>) content.get("parts");
                    return (String) parts.get(0).get("text");
                })
                .flatMap(response -> save("gemini", prompt, response))
                .doOnSuccess(r -> log.info("Respuesta Gemini guardada correctamente"))
                .doOnError(e -> log.error("Error al llamar Gemini: {}", e.getMessage()));
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