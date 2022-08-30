package com.springboot.jpa.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.springboot.jpa.models.entity.Factura;

public interface IFacturaDao extends CrudRepository<Factura, Long>{

}
