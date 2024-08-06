package com.demo.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.demo.usuario.entities.Usuario;
import com.demo.usuario.repositories.UsuarioRepositorie;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private UsuarioRepositorie usuarioRepositorie;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepositorie.findByName(username)
				.orElseThrow(() -> new UsernameNotFoundException("No existe: " + username));
		
		List<SimpleGrantedAuthority> grantedAuthority = new ArrayList<>();
		usuario.getRoles()
			.forEach(role -> grantedAuthority.add(new SimpleGrantedAuthority("ROLE_"+role.getName().name())));
		usuario.getRoles().stream()
			.flatMap(role -> role.getPermisos().stream())
			.forEach(permiso -> grantedAuthority.add(new SimpleGrantedAuthority(permiso.getName().name())));
			
		
		return new User(usuario.getName(), usuario.getPassword(),
				usuario.isEnabled(), usuario.isAccountNoExpired(), usuario.isCredentialNoExpired(), usuario.isAccountNoLocked(),
				grantedAuthority);
	}

}
