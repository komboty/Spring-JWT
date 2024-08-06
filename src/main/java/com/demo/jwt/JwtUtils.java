package com.demo.jwt;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

@Component
public class JwtUtils {
	@Value("${jwt.key}")
	private String privateKey;
	@Value("${jwt.user.generator}")
	private String userGenerator;
	
	
	public String createToken(Authentication authentication) {
		String username = authentication.getPrincipal().toString();
		String authorities = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));
		
		return JWT.create()
				.withIssuer(userGenerator) // User Backend generador de JWT
				.withSubject(username)
				.withClaim("authorities", authorities)
				.withIssuedAt(new Date()) // Fecha que se genera el JWT
				.withNotBefore(new Date(System.currentTimeMillis())) // Cuando el JWT empieza su valides
				.withExpiresAt(new Date(System.currentTimeMillis() + 1800000)) // Se vence el JWT
				.withJWTId(UUID.randomUUID().toString())
                .sign(Algorithm.HMAC256(privateKey));
	}
	
	public DecodedJWT validateToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(privateKey))
                    .withIssuer(userGenerator)
                    .build();

            return verifier.verify(token);
            
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("JwtUtils.validateToken(): Token invalid");
        }
    }

//    public String extractUsername(DecodedJWT decodedJWT){
//        return decodedJWT.getSubject().toString();
//    }
//
//    public Claim getSpecificClaim(DecodedJWT decodedJWT, String claimName) {
//        return decodedJWT.getClaim(claimName);
//    }
}
