package com.example.test.service;


import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ExternalQuoteService {

    private final WebClient externalApiClient;

    public ExternalQuoteService(WebClient externalApiClient) {
        this.externalApiClient = externalApiClient;
    }

    public Mono<String> fetchQuote() {
        return externalApiClient.get()
                .uri("/quote")
                .retrieve()
                .bodyToMono(String.class);
    }
}
