package com.demo.auth.dtos;

public record AuthResponse(
        String username,
        String message,
        String jwt,
        Boolean status
        ) {}
