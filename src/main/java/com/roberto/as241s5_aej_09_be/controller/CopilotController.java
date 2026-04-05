package com.roberto.as241s5_aej_09_be.controller;

import com.roberto.as241s5_aej_09_be.model.QueryRecord;
import com.roberto.as241s5_aej_09_be.repository.QueryRepository;
import com.roberto.as241s5_aej_09_be.service.CopilotService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    @GetMapping("/history")
    public Flux<QueryRecord> history() {
        return repository.findByApiSource("copilot");
    }
}