package com.springboot.jpa.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.springboot.jpa.models.entity.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario, Long>{

	public Usuario findByUsername(String username);
}
