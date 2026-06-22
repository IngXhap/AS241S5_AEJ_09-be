package com.roberto.as241s5_aej_09_be.controller;

import com.roberto.as241s5_aej_09_be.model.QueryRecord;
import com.roberto.as241s5_aej_09_be.repository.QueryRepository;
import com.roberto.as241s5_aej_09_be.service.CopilotService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/copilot")
@RequiredArgsConstructor
public class CopilotController {

    private final CopilotService service;
    private final QueryRepository repository;

    @PostMapping("/ask")
    public Mono<ResponseEntity<String>> ask(@RequestBody Map<String, String> body) {
        return service.ask(body.get("prompt"))
                .map(ResponseEntity::ok);
    }


    @PutMapping("/{id}")
public Mono<ResponseEntity<QueryRecord>> update(
        @PathVariable Long id,
        @RequestBody Map<String, String> body) {
    return repository.findByIdAndApiSource(id, "copilot")
            .flatMap(record -> service.ask(body.get("prompt"))
                    .flatMap(aiResponse -> {
                        record.setPrompt(body.get("prompt"));
                        record.setResponse(aiResponse);
                        record.setUpdatedAt(LocalDateTime.now());
                        return repository.save(record);
                    }))
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
}

@DeleteMapping("/{id}")
public Mono<ResponseEntity<Object>> delete(@PathVariable Long id) {
    return repository.findByIdAndApiSource(id, "copilot") // "gemini" en GeminiController
            .flatMap(record -> {
                record.setDeleted(true);
                record.setUpdatedAt(LocalDateTime.now());
                return repository.save(record);
            })
            .map(r -> ResponseEntity.ok().build())
            .defaultIfEmpty(ResponseEntity.notFound().build());
}

// Cambiar history para excluir eliminados
@GetMapping("/history")
public Flux<QueryRecord> history() {
    return repository.findByApiSourceAndDeletedFalse("copilot");
}


}