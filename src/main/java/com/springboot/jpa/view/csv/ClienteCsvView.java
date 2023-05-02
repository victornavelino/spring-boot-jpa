/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.springboot.jpa.view.csv;

import com.springboot.jpa.models.entity.Cliente;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

/**
 *
 * @author hugo
 */
@Component("listar")
public class ClienteCsvView extends AbstractView {

    public ClienteCsvView() {
        setContentType("text/csv");
    } 

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("content-disposition", "attachment; filename=\"clientes.csv\"");
        response.setContentType(getContentType());
        Page<Cliente> clientes =(Page<Cliente>)model.get("clientes");
        ICsvBeanWriter beanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] encabezado = {"id", "nombre", "apellido", "email", "createAt"};
        beanWriter.writeHeader(encabezado);
        for(Cliente cliente:clientes){
            beanWriter.write(cliente, encabezado);
        }
        beanWriter.close();
    }

}
