/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.springboot.jpa.view.pdf;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.springboot.jpa.models.entity.Factura;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

/**
 *
 * @author hugo
 */
@Component("factura/ver")
public class FacturaPdfView extends AbstractPdfView {

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Factura factura = (Factura) model.get("factura");
        
        PdfPTable tablaClientes = new PdfPTable(1);
        tablaClientes.setSpacingAfter(20);
        tablaClientes.addCell("Datos del Cliente");
        tablaClientes.addCell(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
        tablaClientes.addCell(factura.getCliente().getEmail());

        PdfPTable tablaDatosFactura = new PdfPTable(1);
        tablaDatosFactura.setSpacingAfter(20);
        tablaDatosFactura.addCell("Datos de la Factura");
        tablaDatosFactura.addCell("Folio: "+factura.getId());
        tablaDatosFactura.addCell("Descripcion: "+factura.getDescripcion());
        tablaDatosFactura.addCell("Fecha: "+factura.getCreateAt());
        
        document.add(tablaClientes);
        document.add(tablaDatosFactura);
    }

}
