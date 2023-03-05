package com.springboot.jpa.controllers;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
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
import com.springboot.jpa.models.service.IUploadFileService;
import com.springboot.jpa.util.paginator.PageRender;

@Controller
@SessionAttributes("cliente")
public class ClienteController {

	@Autowired
	private IClienteService clienteService;
	
	@Autowired
	private IUploadFileService uploadFileService;
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	
    @Secured("ROLE_USER")
	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

		Cliente cliente = clienteService.findOne(id);
		if (cliente == null) {
			flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
			return "redirect:/listar";

		}
		model.put("cliente", cliente);
		model.put("titulo", "Detalle del Cliente: " + cliente.getNombre());
		return "ver";
	}

    @Secured("ROLE_USER")
	@GetMapping(value = "/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename) {
		Resource recurso=null;
		try {
			recurso = uploadFileService.load(filename);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseEntity.ok().header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
				"attachmen; filename=\"" + recurso.getFilename() + "\"").body(recurso);
	}

	@RequestMapping(value = { "/listar", "/" }, method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model, 
			Authentication authentication, HttpServletRequest request) {

		if(authentication!=null) {
			logger.info("Hola usuario autenticado, tu username es:".concat(authentication.getName()));
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if(auth!=null) {
			logger.info("Hola usuario autenticado con SecurityContextHolder.getContext().getAuthentication(), "
					+ "tu username es:".concat(auth.getName()));
		}
		// 1--UNA FORMA DE VER EL ROL DEL USUARIO
		if(hasRole("ROLE_ADMIN")) {
			logger.info("hola ".concat(auth.getName()).concat(" Tienes acceso!"));
		}else {
			logger.info("hola ".concat(auth.getName()).concat(" No Tienes acceso!"));
		}
		//2---OTRA FORMA DE VER EL ROL DEL USUARIO
		SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request, "");
		if(securityContext.isUserInRole("ROLE_ADMIN")) {
			logger.info("Hola Usando SecurityContextHolderAwareRequestWrapper: usuario autenticado, tu username es:"
					.concat(auth.getName()).concat(" Tienes acceso!"));
		}else {
			logger.info("Hola Usando SecurityContextHolderAwareRequestWrapper: usuario autenticado, tu username es:"
					.concat(auth.getName()).concat(" No Tienes acceso!"));
		}
		// 3 --- OTRA FORMA DE VER EL ROL DEL USUARIO
		if(request.isUserInRole("ROLE_ADMIN")) {
			logger.info("Hola Usando HttpServletRequest: usuario autenticado, tu username es:"
					.concat(auth.getName()).concat(" Tienes acceso!"));
		}else {
			logger.info("Hola Usando HttpServletRequest: usuario autenticado, tu username es:"
					.concat(auth.getName()).concat(" No Tienes acceso!"));
		}
		//aqui sigue el codigo
		Pageable pageRequest = PageRequest.of(page, 4);
		Page<Cliente> clientes = clienteService.findAll(pageRequest);

		PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);
		model.addAttribute("titulo", "Listado de Clientes");
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);
		return "listar";
	}
    
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form")
	public String crear(Map<String, Object> model) {
		Cliente cliente = new Cliente();
		model.put("titulo", "Formulario de Cliente");
		model.put("cliente", cliente);
		return "form";
	}
    
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model,
			@RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status) {
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de Cliente");
			return "form";
		}
		if (!foto.isEmpty()) {

			if (cliente.getId() != null && cliente.getId() > 0 && cliente.getFoto() != null
					&& cliente.getFoto().length() > 0) {
					uploadFileService.delete(cliente.getFoto());
			}
			String uniqueFileName =null;
			try {
				uniqueFileName = uploadFileService.copy(foto);
			} catch (IOException e) { 
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			flash.addFlashAttribute("info", "Imagen subida Correctamente " + uniqueFileName);
			cliente.setFoto(uniqueFileName);

		}
		String mensajeFlash = (cliente.getId() != null) ? "Cliente Editado Con Exito!" : "Cliente Creado con Exito!";
		clienteService.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:listar";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/form/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		Cliente cliente = null;
		if (id > 0) {
			cliente = clienteService.findOne(id);
		} else {
			flash.addFlashAttribute("error", "El ID de Cliente no encontrado");
			return "redirect:/listar";
		}
		model.put("cliente", cliente);
		model.put("titulo", "Formulario de Cliente");
		return "form";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
		if (id > 0) {
			Cliente cliente = clienteService.findOne(id);
			clienteService.delete(id);
			flash.addFlashAttribute("success", "Cliente Eliminado Con Exito");

				if (uploadFileService.delete(cliente.getFoto())) {
					flash.addAttribute("info", "Foto: " + cliente.getFoto() + " Eliminada");
				}
		}

		return "redirect:/listar";
	}
	
	private boolean hasRole(String role) {
		SecurityContext context = SecurityContextHolder.getContext();
		
		if(context ==null) {
			return false;
		}
		Authentication auth = context.getAuthentication();
		if(auth ==null) {
			return false;
		}
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
	    System.out.println(authorities);
	    
	    return authorities.contains(new SimpleGrantedAuthority(role));
		/*for(GrantedAuthority authority:authorities) {
			if(role.equals(authority.getAuthority())) {
				logger.info("hola usuario: ".concat(auth.getName()).concat(" Tu rol es: ").concat(authority.getAuthority()));
				return true;
			}
		}
		return false;*/
	}

}
