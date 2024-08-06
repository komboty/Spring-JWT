package com.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.demo.jwt.JwtUtils;
import com.demo.jwt.JwtValidatorFilter;
import com.demo.usuario.entities.PermisoEnum;
import com.demo.usuario.entities.RolEnum;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	private JwtUtils jwtUtils;
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurityty) throws Exception {
		return httpSecurityty
				.csrf(csrf -> csrf.disable())
				.httpBasic(Customizer.withDefaults())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(http -> {
                    http.requestMatchers("/auth/**").permitAll();

                    http.requestMatchers(HttpMethod.POST, "/private").hasAnyRole(RolEnum.ADMIN.name(), RolEnum.USER.name());
                    http.requestMatchers(HttpMethod.DELETE, "/private").hasAnyAuthority(PermisoEnum.DELETE.name());

                    http.anyRequest().denyAll();
                })
				.addFilterBefore(new JwtValidatorFilter(jwtUtils), BasicAuthenticationFilter.class)
				.build();
	}
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	@Bean
	AuthenticationProvider authenticationProvider(UserDetailsServiceImpl userDetailsServiceImpl) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder());
		provider.setUserDetailsService(userDetailsServiceImpl);
		return provider;
	}
		
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
