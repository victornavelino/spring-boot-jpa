/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.springboot.jpa.view.xml;

import com.springboot.jpa.models.entity.Cliente;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author hugo
 */
@XmlRootElement(name = "clientes")
public class ClienteList {
    
    @XmlElement(name = "cliente")
    public List<Cliente> clientes;

    public ClienteList(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    public ClienteList() {
    }

    public List<Cliente> getClientes() {
        return clientes;
    }
    
    
}
