package com.demo.usuario.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.demo.usuario.entities.Permiso;

@Repository
public interface PermisoRepositorie extends CrudRepository<Permiso, Long> {

}
