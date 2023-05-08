/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.springboot.jpa.controllers;

import com.springboot.jpa.models.service.IClienteService;
import com.springboot.jpa.view.xml.ClienteList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author hugo
 */
@RestController
@RequestMapping("/api/clientes")
public class ClienteRestController {

    @Autowired
    private IClienteService clienteService;

    @RequestMapping("/listar")
    public ClienteList listar() {

        return new ClienteList(clienteService.findAll());
    }

}
