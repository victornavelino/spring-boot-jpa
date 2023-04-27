/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.springboot.jpa.view.xlsx;

import com.springboot.jpa.models.entity.Factura;
import com.springboot.jpa.models.entity.ItemFactura;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

/**
 *
 * @author hugo
 */
@Component("factura/ver.xlsx")
public class FacturaXlsxView extends AbstractXlsxView{

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Factura factura = (Factura)model.get("factura");
        Sheet sheet = workbook.createSheet("Factura Spring");
        sheet.createRow(0).createCell(0).setCellValue("Datos del Cliente");
        sheet.createRow(1).createCell(0).setCellValue(factura.getCliente().getNombre()+", "+factura.getCliente().getApellido());
        sheet.createRow(2).createCell(0).setCellValue(factura.getCliente().getEmail());
        sheet.createRow(4).createCell(0).setCellValue("Datos de la Factura");
        sheet.createRow(5).createCell(0).setCellValue("Folio: "+factura.getId());
        sheet.createRow(6).createCell(0).setCellValue("Descripcion: "+factura.getDescripcion());
        sheet.createRow(7).createCell(0).setCellValue("Fecha: "+factura.getCreateAt());
        
        Row header = sheet.createRow(9);
        header.createCell(0).setCellValue("Producto");
        header.createCell(1).setCellValue("Precio");
        header.createCell(2).setCellValue("Cantidad");
        header.createCell(3).setCellValue("Total");
        
        int rownum = 10;
        
        for(ItemFactura itemFactura: factura.getItems()){
            Row fila = sheet.createRow(rownum++);
            fila.createCell(0).setCellValue(itemFactura.getProducto().getNombre());
            fila.createCell(1).setCellValue(itemFactura.getProducto().getPrecio().toString());
            fila.createCell(2).setCellValue(itemFactura.getCantidad());
            fila.createCell(3).setCellValue(itemFactura.calcularImporte());
        }
        Row filaTotal = sheet.createRow(rownum);
        filaTotal.createCell(2).setCellValue("TOTAL: ");
        filaTotal.createCell(3).setCellValue(factura.getTotal());
        
    }
    
}
