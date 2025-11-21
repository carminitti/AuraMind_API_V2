package com.auramind.api.dto;

public class AuthDTOs {

    public record RegisterRequest(
            String email,
            String displayName,
            String password
    ) {}

    public record LoginRequest(
            String email,
            String password
    ) {}

    public record AuthResponse(
            Long id,
            String email,
            String displayName,
            String token
    ) {}
}
