package com.example.test;

import com.example.test.api.GreetingController.Greeting;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(OutputCaptureExtension.class)
class GreetingControllerTest {

    static WireMockServer wireMock = new WireMockServer(8089);

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("external.api.base-url", () -> "http://localhost:8089");
    }

    @BeforeAll
    static void start() {
        wireMock.start();
    }

    @AfterAll
    static void stop() {
        wireMock.stop();
    }

    @BeforeEach
    void stub() {
        wireMock.resetAll();
        wireMock.stubFor(get(urlEqualTo("/quote"))
                .willReturn(okJson("“Testing leads to failure, and failure leads to understanding.”")));
    }

    @Autowired
    WebTestClient webTestClient;

    @LocalServerPort
    int port;

    @Test
    void greet_returns_message_and_quote() {
        webTestClient.get()
                .uri("http://localhost:" + port + "/api/greet?name=Noura")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Greeting.class)
                .value(g -> {
                    Assertions.assertEquals("Hello, Noura!", g.message());
                    Assertions.assertTrue(g.quote().contains("Testing"));
                });
    }
}
