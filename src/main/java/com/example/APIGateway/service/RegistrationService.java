package com.example.APIGateway.service;

import com.example.APIGateway.client.AuthClient;
import com.example.APIGateway.client.UserClient;
import com.example.APIGateway.dto.AuthRequest;
import com.example.APIGateway.dto.RegistrationRequest;
import com.example.APIGateway.dto.UserResponse;
import com.example.APIGateway.exception.RegistrationFailedException;
import com.example.APIGateway.mapper.RegistrationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final AuthClient authClient;
    private final UserClient userClient;
    private final RegistrationMapper mapper;

    public Mono<UserResponse> register(RegistrationRequest request) {
        return userClient.create(mapper.toUserRequest(request))
                .onErrorResume(e -> Mono.error(new RegistrationFailedException("Registration failed")))
                .flatMap(user -> {
                    AuthRequest authRequest = mapper.toAuthRequest(request);
                    authRequest.setUserId(user.getId());

                    return authClient.register(authRequest)
                            .thenReturn(user)
                            .onErrorResume(e ->
                                    userClient.rollback(user.getId())
                                            .then(Mono.error(new RegistrationFailedException("Registration failed"))));
                });
    }
}
