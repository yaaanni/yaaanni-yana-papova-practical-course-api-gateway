package com.example.APIGateway.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class CardResponse {

    private Long id;
    private String number;
    private String holder;
    private LocalDate expirationDate;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}