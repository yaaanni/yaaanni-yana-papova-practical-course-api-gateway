package com.example.APIGateway.client;

import com.example.APIGateway.dto.AuthRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AuthClient {

    private final WebClient webClient;

    public AuthClient(@Qualifier("authWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Void> register(AuthRequest request) {
        return webClient.post()
                .uri("/auth/register")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
