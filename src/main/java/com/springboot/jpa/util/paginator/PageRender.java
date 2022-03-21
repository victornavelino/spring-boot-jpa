package com.springboot.jpa.util.paginator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageRender<T> {

	private String url;
	private Page<T> page;
	private int totalPaginas;
	private int numElementosPorPagina;
	private int paginaActual;
	
	private List<PageItem> paginas;
	

	public PageRender(String url, Page<T> page) {
		this.url = url;
		this.page = page;
		this.paginas = new ArrayList<>();
		numElementosPorPagina = page.getSize();
		totalPaginas= page.getTotalPages();
		paginaActual = page.getNumber() + 1;
		
		int desde, hasta;
		if (totalPaginas <= numElementosPorPagina) {
			desde = 1;
			hasta =totalPaginas;
		} else {
			if (paginaActual <= numElementosPorPagina/2) {
				desde = 1;
				hasta = numElementosPorPagina;
			} else if(paginaActual >= totalPaginas - numElementosPorPagina/2) {
				
				desde= totalPaginas -numElementosPorPagina +1;
				hasta = numElementosPorPagina;
				
			}else {
					desde= paginaActual -numElementosPorPagina/2;
					hasta = numElementosPorPagina;
				
				
				}		
		}
	}
	
   
}
