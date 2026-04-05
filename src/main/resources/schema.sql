CREATE TABLE IF NOT EXISTS query_record (
    id       BIGSERIAL PRIMARY KEY,
    api_source VARCHAR(50)  NOT NULL,
    prompt   TEXT          NOT NULL,
    response TEXT          NOT NULL,
    created_at TIMESTAMP   DEFAULT CURRENT_TIMESTAMP
);