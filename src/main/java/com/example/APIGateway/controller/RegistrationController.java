package com.example.APIGateway.controller;

import com.example.APIGateway.dto.RegistrationRequest;
import com.example.APIGateway.dto.UserResponse;
import com.example.APIGateway.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/register")
    public Mono<ResponseEntity<UserResponse>> register(@RequestBody RegistrationRequest request) {
        return registrationService.register(request)
                .map(user -> ResponseEntity.status(201).body(user));
    }
}
