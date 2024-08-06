package com.demo.auth.dtos;

import java.util.List;

public record AuthCreateUserRequest(
		String username,
        String password,
        List<String> roles
        ) { }
