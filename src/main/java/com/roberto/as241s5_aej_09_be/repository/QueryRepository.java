package com.roberto.as241s5_aej_09_be.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import com.roberto.as241s5_aej_09_be.model.QueryRecord;

@Repository
public interface QueryRepository extends ReactiveMongoRepository<QueryRecord, String> {

    // Buscar todos los registros por fuente (copilot o gemini)
    Flux<QueryRecord> findByApiSource(String apiSource);
}