package com.roberto.as241s5_aej_09_be.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "query_record")
public class QueryRecord {

    @Id
    private String id;

    @Field("api_source")
    private String apiSource;

    @Field("prompt")
    private String prompt;

    @Field("response")
    private String response;

    @Field("created_at")
    private LocalDateTime createdAt;
}