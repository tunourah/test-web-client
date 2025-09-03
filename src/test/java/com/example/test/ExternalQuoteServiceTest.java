package com.example.test;


import com.example.test.service.ExternalQuoteService;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import reactor.test.StepVerifier;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest
class ExternalQuoteServiceTest {

    static WireMockServer wireMock = new WireMockServer(8089);

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("external.api.base-url", () -> "http://localhost:8089");
    }

    @BeforeAll
    static void start() { wireMock.start(); }

    @AfterAll
    static void stop() { wireMock.stop(); }

    @BeforeEach
    void stub() {
        wireMock.resetAll();
        wireMock.stubFor(get(urlEqualTo("/quote"))
                .willReturn(ok().withBody("Keep going!")));
    }

    @Autowired
    ExternalQuoteService service;

    @Test
    void fetchQuote_returns_stubbed_value() {
        StepVerifier.create(service.fetchQuote())
                .expectNext("Keep going!")
                .verifyComplete();
    }
}
