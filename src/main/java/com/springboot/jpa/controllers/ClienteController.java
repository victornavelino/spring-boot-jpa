package com.springboot.jpa.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springboot.jpa.models.entity.Cliente;
import com.springboot.jpa.models.service.IClienteService;
import com.springboot.jpa.util.paginator.PageRender;

@Controller
@SessionAttributes("cliente")
public class ClienteController {
	
	@Autowired
	private IClienteService clienteService;
	
	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value="id")Long id, Map<String, Object> model, RedirectAttributes flash) {
		
		Cliente cliente = clienteService.findOne(id);
		if(cliente==null) {
			flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
			return "redirect:/listar";
			
		}
		model.put("cliente",cliente);
		model.put("titulo","Detalle del Cliente: "+cliente.getNombre());
		return "ver";
	}
	
	
	@RequestMapping(value = {"/listar","/"}, method = RequestMethod.GET)
	public String listar(@RequestParam(name="page", defaultValue = "0" ) int page,  Model model) {
		
		Pageable pageRequest=PageRequest.of(page, 4);
		Page<Cliente> clientes= clienteService.findAll(pageRequest);
		
		PageRender<Cliente> pageRender = new PageRender<>("/listar",clientes);	
		model.addAttribute("titulo", "Listado de Clientes");
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);
		return "listar";
	}
	
	@RequestMapping(value ="/form")
	public String crear(Map<String, Object> model) {
		Cliente cliente = new Cliente();
		model.put("titulo", "Formulario de Cliente");
		model.put("cliente", cliente);
		return "form";
	}
	
	@RequestMapping(value="/form", method = RequestMethod.POST)
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model, @RequestParam("file") MultipartFile foto,  RedirectAttributes flash, SessionStatus status) {
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de Cliente");
			return "form";			
		}
		if(!foto.isEmpty()) {
			
			String rootPath = "C://Temp//uploads"; 
			
			try {
				byte[] bytes = foto.getBytes();
				Path rutaCompleta = Paths.get(rootPath + "//" + foto.getOriginalFilename());
				Files.write(rutaCompleta, bytes);
				flash.addFlashAttribute("info","Imagen subida Correctamente"+ foto.getOriginalFilename());
				cliente.setFoto(foto.getOriginalFilename());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String mensajeFlash = (cliente.getId()!=null)? "Cliente Editado Con Exito!": "Cliente Creado con Exito!";
		clienteService.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:listar";
	}
	
	@RequestMapping(value = "/form/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		Cliente cliente=null;
		if (id > 0) {
			cliente=clienteService.findOne(id);
		} else {
			flash.addFlashAttribute("error", "El ID de Cliente no encontrado");
			return "redirect:/listar";
		}
		model.put("cliente", cliente);
		model.put("titulo", "Formulario de Cliente");
	return "form";	
	}
	
	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
		if (id > 0) {
			clienteService.delete(id);
			flash.addFlashAttribute("success", "Cliente Eliminado Con Exito");
		} 
		
		return "redirect:/listar";
	}

}
