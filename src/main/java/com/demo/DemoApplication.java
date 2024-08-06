package com.demo;

import java.util.List;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.demo.usuario.entities.Permiso;
import com.demo.usuario.entities.PermisoEnum;
import com.demo.usuario.entities.Rol;
import com.demo.usuario.entities.RolEnum;
import com.demo.usuario.entities.Usuario;
import com.demo.usuario.repositories.PermisoRepositorie;
import com.demo.usuario.repositories.RolRepositorie;
import com.demo.usuario.repositories.UsuarioRepositorie;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner init(
			PermisoRepositorie permisoRepositorie, RolRepositorie rolRepositorie,
			UsuarioRepositorie usuarioRepositorie, PasswordEncoder passwordEncoder) {
		
		return args -> {
			Permiso permisoCreate = Permiso.builder().name(PermisoEnum.CREATE).build();
			Permiso permisoRead = Permiso.builder().name(PermisoEnum.READ).build();
			Permiso permisoUpdate = Permiso.builder().name(PermisoEnum.UPDATE).build();
			Permiso permisoDelete = Permiso.builder().name(PermisoEnum.DELETE).build();
			permisoRepositorie.saveAll(List.of(permisoCreate,permisoRead,permisoUpdate,permisoDelete));
			
			Rol rolAdmin = Rol.builder().name(RolEnum.ADMIN)
					.permisos(Set.of(permisoCreate,permisoRead,permisoUpdate,permisoDelete)).build();
			Rol rolUser = Rol.builder().name(RolEnum.USER)
					.permisos(Set.of(permisoCreate,permisoRead)).build();
			Rol rolInvited = Rol.builder().name(RolEnum.INVITED)
					.permisos(Set.of(permisoRead)).build();
			rolRepositorie.saveAll(List.of(rolAdmin,rolUser,rolInvited));
			
			Usuario userAdmin = Usuario.builder().name("admin").password(passwordEncoder.encode("1234"))
					.isEnabled(true).accountNoExpired(true).accountNoLocked(true).credentialNoExpired(true)
					.roles(Set.of(rolAdmin)).build();
			Usuario userUser = Usuario.builder().name("user").password(passwordEncoder.encode("1234"))
					.isEnabled(true).accountNoExpired(true).accountNoLocked(true).credentialNoExpired(true)
					.roles(Set.of(rolUser)).build();
			Usuario userInvited = Usuario.builder().name("invited").password(passwordEncoder.encode("1234"))
					.isEnabled(true).accountNoExpired(true).accountNoLocked(true).credentialNoExpired(true)
					.roles(Set.of(rolInvited)).build();			
			usuarioRepositorie.saveAll(List.of(userAdmin,userUser,userInvited));
		};
	}
}
