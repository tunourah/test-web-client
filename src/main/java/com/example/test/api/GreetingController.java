package com.example.test.api;


import com.example.test.service.ExternalQuoteService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class GreetingController {

    private final ExternalQuoteService externalQuoteService;

    public GreetingController(ExternalQuoteService externalQuoteService) {
        this.externalQuoteService = externalQuoteService;
    }

    @GetMapping(value = "/greet", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Greeting> greet(@RequestParam(defaultValue = "World") String name) {
        return externalQuoteService.fetchQuote()
                .defaultIfEmpty("No quote today.")
                .map(q -> new Greeting("Hello, " + name + "!", q));
    }

    public record Greeting(String message, String quote) {}
}
