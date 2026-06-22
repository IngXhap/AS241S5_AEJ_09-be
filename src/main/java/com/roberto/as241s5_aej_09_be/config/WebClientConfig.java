package com.roberto.as241s5_aej_09_be.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${rapidapi.key}")
    private String rapidApiKey;

    @Value("${copilot.api.url}")
    private String copilotUrl;

    @Value("${copilot.api.host}")
    private String copilotHost;

    @Value("${gemini.api.url}")
    private String geminiUrl;

    @Value("${gemini.api.host}")
    private String geminiHost;

    @Bean("copilotClient")
    public WebClient copilotClient() {
        return WebClient.builder()
                .baseUrl(copilotUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("x-rapidapi-key", rapidApiKey)
                .defaultHeader("x-rapidapi-host", copilotHost)
                .build();
    }

    @Bean("geminiClient")
    public WebClient geminiClient() {
        return WebClient.builder()
                .baseUrl(geminiUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("x-rapidapi-key", rapidApiKey)
                .defaultHeader("x-rapidapi-host", geminiHost)
                .build();
    }
}