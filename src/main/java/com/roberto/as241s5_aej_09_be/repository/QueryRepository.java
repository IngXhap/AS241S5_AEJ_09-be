package com.roberto.as241s5_aej_09_be.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.roberto.as241s5_aej_09_be.model.QueryRecord;

@Repository
public interface QueryRepository extends ReactiveCrudRepository<QueryRecord, Long> {

    // Buscar todos los registros por fuente (openai o gemini)
    Flux<QueryRecord> findByApiSource(String apiSource);


    Flux<QueryRecord> findByApiSourceAndDeletedFalse(String apiSource);
    Mono<QueryRecord> findByIdAndApiSource(Long id, String apiSource);
}
