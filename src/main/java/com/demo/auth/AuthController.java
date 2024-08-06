package com.demo.auth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.auth.dtos.AuthCreateUserRequest;
import com.demo.auth.dtos.AuthLoginRequest;
import com.demo.auth.dtos.AuthResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	private AuthService authService;
	
	@PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthLoginRequest authLoginRequest){		 
		try {
			return new ResponseEntity<>(authService.loginUser(authLoginRequest), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new AuthResponse(null, e.getMessage(), null, null), HttpStatus.BAD_REQUEST);
		}
    }
	
	@PostMapping("/signup")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthCreateUserRequest userRequest){
		try {
			return new ResponseEntity<>(authService.createUser(userRequest), HttpStatus.CREATED);					
		} catch (Exception e) {
			return new ResponseEntity<>(new AuthResponse(null, e.getMessage(), null, null), HttpStatus.BAD_REQUEST);
		}
        
    }
}
