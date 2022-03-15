package com.springboot.jpa.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.springboot.jpa.models.entity.Cliente;

public interface IClienteDao extends CrudRepository<Cliente, Long>{
	

}
