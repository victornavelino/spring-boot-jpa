package com.springboot.jpa.controllers;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springboot.jpa.models.entity.Cliente;
import com.springboot.jpa.models.entity.Factura;
import com.springboot.jpa.models.entity.ItemFactura;
import com.springboot.jpa.models.entity.Producto;
import com.springboot.jpa.models.service.IClienteService;
import org.springframework.ui.Model;

@Secured("ROLE_ADMIN")
@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
public class FacturaController {

    @Autowired
    private IClienteService clienteService;

    private final Logger log = LoggerFactory.getLogger(getClass());
    
    @GetMapping("/ver/{id}")
    public String ver(@PathVariable(value ="id") Long id, Model model, RedirectAttributes flash){
        Factura factura = clienteService.findFacturaById(id);
        if (factura ==null) {
            flash.addFlashAttribute("error", "La factura no existe en la Base de datos!");
            return "redirect:/listar";
        }
        model.addAttribute("factura", factura);
        model.addAttribute("titulo", "Factura: "+ factura.getDescripcion());
        return "factura/ver";
    }

    @GetMapping("/form/{clienteId}")
    public String crear(@PathVariable(value = "clienteId") Long clienteId, Map<String, Object> model, RedirectAttributes flash) {

        Cliente cliente = clienteService.findOne(clienteId);
        if (cliente == null) {
            flash.addAttribute("error", "El cliente no existe");
            return "redirect:/listar";
        }
        Factura factura = new Factura();
        factura.setCliente(cliente);
        model.put("factura", factura);
        model.put("titulo", "Crear Factura");
        return "/factura/form";
    }

    @GetMapping(value = "/cargar-productos/{term}", produces = {"application/json"})
    public @ResponseBody
    List<Producto> cargarProductos(@PathVariable String term) {
        return clienteService.findByNombre(term);
    }

    @PostMapping("/form")
    public String guardar(Factura factura, @RequestParam(name = "item_id[]", required = false) Long[] itemId,
            @RequestParam(name = "cantidad[]", required = false) Integer[] cantidad, RedirectAttributes flash,
            SessionStatus status) {
        for (int i = 0; i < itemId.length; i++) {
            Producto producto = clienteService.findProductoById(itemId[i]);
            ItemFactura item = new ItemFactura();
            item.setCantidad(cantidad[i]);
            item.setProducto(producto);
            factura.addItemFactura(item);
            log.info("ID: " + itemId[i].toString() + ", Cantidad: " + cantidad[i].toString());
        }
        clienteService.saveFactura(factura);
        status.setComplete();
        flash.addFlashAttribute("success", "Factura creada con exito!");
        return "redirect:/ver/" + factura.getCliente().getId();
    }

}
