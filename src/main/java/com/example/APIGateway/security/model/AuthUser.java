package com.example.APIGateway.security.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthUser {
    private final Long userId;
    private final String role;
}
