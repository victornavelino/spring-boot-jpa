package com.springboot.jpa.models.dao;

import java.util.List;

import com.springboot.jpa.models.entity.Cliente;

public interface IClienteDao {
	
	public List<Cliente> findAll();
	
	public void save(Cliente cliente);
	
	public Cliente findOne(Long id);

}
