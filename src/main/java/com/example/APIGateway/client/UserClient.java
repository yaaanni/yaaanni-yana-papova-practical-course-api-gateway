package com.example.APIGateway.client;

import com.example.APIGateway.dto.UserRequest;
import com.example.APIGateway.dto.UserResponse;
import com.example.APIGateway.security.service.JwtService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class UserClient {

    private final WebClient webClient;
    private final JwtService jwtService;

    public UserClient(@Qualifier("userWebClient") WebClient webClient, JwtService jwtService) {
        this.webClient = webClient;
        this.jwtService = jwtService;
    }

    public Mono<UserResponse> create(UserRequest request) {
        return webClient.post()
                .uri("/users")
                .header("Authorization", "Bearer " + jwtService.generateToken("service",0L,"ADMIN"))
                .bodyValue(request)
                .retrieve()
                .bodyToMono(UserResponse.class);
    }

    public Mono<Void> rollback(Long id) {
        return webClient.delete()
                .uri("/users/{id}", id)
                .header("Authorization", "Bearer " + jwtService.generateToken("service",0L,"ADMIN"))
                .retrieve()
                .bodyToMono(Void.class);
    }
}
