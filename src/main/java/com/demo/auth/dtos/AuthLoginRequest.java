package com.demo.auth.dtos;

public record AuthLoginRequest(
		String username,
        String password
        ) {}
