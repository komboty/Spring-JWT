package com.demo.usuario.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.demo.usuario.entities.Rol;

@Repository
public interface RolRepositorie extends CrudRepository<Rol, Long> {
	Set<Rol> findByNameIn(List<String> rolNames);
}
