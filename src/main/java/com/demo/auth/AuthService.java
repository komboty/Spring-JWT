package com.demo.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.demo.auth.dtos.AuthCreateUserRequest;
import com.demo.auth.dtos.AuthLoginRequest;
import com.demo.auth.dtos.AuthResponse;
import com.demo.jwt.JwtUtils;
import com.demo.usuario.entities.Rol;
import com.demo.usuario.entities.Usuario;
import com.demo.usuario.repositories.RolRepositorie;
import com.demo.usuario.repositories.UsuarioRepositorie;

@Service
public class AuthService {
	
	@Autowired	private UserDetailsService userDetailsService;
	@Autowired	private PasswordEncoder passwordEncoder;
	@Autowired	private JwtUtils jwtUtils;
	@Autowired	private RolRepositorie rolRepositorie;
	@Autowired	private UsuarioRepositorie usuarioRepositorie;

	public AuthResponse loginUser(AuthLoginRequest authLoginRequest) {
        String username = authLoginRequest.username();
        String password = authLoginRequest.password();

        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.createToken(authentication);
        return new AuthResponse(username, "Usuario logeado", accessToken, true);
    }
	
	public Authentication authenticate(String username, String password) throws BadCredentialsException {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

//        if (userDetails == null) {        	
//            throw new BadCredentialsException("Invalid username or password");
//        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Password incorrecto");
        }

        return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
    }
	
	public AuthResponse createUser(AuthCreateUserRequest userRequest) {
		
        List<String> roles = userRequest.roles();
        Set<Rol> rolesDB = rolRepositorie.findByNameIn(roles);      
        
//        if (rolesDB.isEmpty()) {
//            throw new IllegalArgumentException("The roles specified does not exist.");
//        }

        Usuario userNew = Usuario.builder()
        		.name(userRequest.username())
        		.password(passwordEncoder.encode(userRequest.password()))
        		.roles(rolesDB)
        		.isEnabled(true).accountNoLocked(true).accountNoExpired(true).credentialNoExpired(true)
        		.build();
        userNew = usuarioRepositorie.save(userNew);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        userNew.getRoles()
        	.forEach(rol -> authorities.add(new SimpleGrantedAuthority("ROLE_".concat(rol.getName().name()))));
        userNew.getRoles().stream()
        	.flatMap(rol -> rol.getPermisos().stream())
        	.forEach(permiso -> authorities.add(new SimpleGrantedAuthority(permiso.getName().name())));

        Authentication authentication = new UsernamePasswordAuthenticationToken(userNew.getName(), userNew.getPassword(), authorities);

        String accessToken = jwtUtils.createToken(authentication);
        return new AuthResponse(userNew.getName(), "Usuario creado", accessToken, true);
    }
}
