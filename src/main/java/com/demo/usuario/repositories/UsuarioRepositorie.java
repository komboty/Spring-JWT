package com.demo.usuario.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.demo.usuario.entities.Usuario;

@Repository
public interface UsuarioRepositorie extends CrudRepository<Usuario, Long> {
	Optional<Usuario> findByName(String name);
	
//	@Query("SELECT u FROM usuario u WHERE u.name = ?")
//	Optional<Usuario> findUsuario(String name);
}
