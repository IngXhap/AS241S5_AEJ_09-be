package com.roberto.as241s5_aej_09_be.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("query_record")
public class QueryRecord {

    @Id
    private Long id;

    @Column("api_source")
    private String apiSource;

    @Column("prompt")
    private String prompt;

    @Column("response")
    private String response;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("deleted")
    @Builder.Default
    private Boolean deleted = false;

    @Column("updated_at")
    private LocalDateTime updatedAt;    

}
