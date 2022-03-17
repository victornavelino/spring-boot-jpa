package com.springboot.jpa.models.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.springboot.jpa.models.entity.Cliente;

public interface IClienteDao extends PagingAndSortingRepository<Cliente, Long>{
	

}
