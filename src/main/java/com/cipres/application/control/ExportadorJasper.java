package com.cipres.application.control;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.vaadin.ui.UI;

import lombok.extern.log4j.Log4j2;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;



@Log4j2
public class ExportadorJasper {
    
    public  void exportarFormatoPDF(List<?> datos, InputStream reportStream, Map<String, Object> params, String fileName) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperPrint jasperPrint = getJasperPrint(datos, reportStream, params, byteArrayOutputStream);
        if (jasperPrint != null) {
            try {
                JRPdfExporter pdfExporter = new JRPdfExporter();
                pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));                
                
                pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(fileName));            
                
                SimplePdfReportConfiguration reportConfig  = new SimplePdfReportConfiguration();
                reportConfig.setSizePageToContent(true);
                SimplePdfExporterConfiguration exportConfig
                    = new SimplePdfExporterConfiguration();
                exportConfig.setMetadataAuthor("cipres");
                exportConfig.setMetadataCreator("cipresinventario");
                exportConfig.setMetadataKeywords(null);
                pdfExporter.setConfiguration(reportConfig);
                pdfExporter.setConfiguration(exportConfig);                

                pdfExporter.exportReport();               
            } catch (Exception e) {
                log.error("Error al exportar a PDF: " + e.getMessage());
            }             
        }
    }

    public JasperPrint getJasperPrint(List<?> datos, InputStream reportStream, Map<String, Object> params, ByteArrayOutputStream byteArrayOutputStream) {
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datos);
        try {
            return JasperFillManager.fillReport(reportStream, params, dataSource);
        } catch (JRException e) {
            // Manejar la excepción específica de JasperReports
            log.error("Error al generar el JasperPrint: " + e.getMessage());
        }
        return null;
    }     
}