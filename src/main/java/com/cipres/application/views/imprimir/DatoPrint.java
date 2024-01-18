package com.cipres.application.views.imprimir;

import org.springframework.beans.factory.annotation.Value;

import com.cipres.application.control.ExportadorJasper;
import com.cipres.application.control.TranslatorService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.shared.Tooltip.TooltipPosition;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;


@Route("datoprint")
public class DatoPrint extends  FormLayout  {
    @Value("${custom.backend.endpoint.url}")
    private String appUrl;

    private final TranslatorService translatorService;


    public <T> DatoPrint(TranslatorService translatorService, @Value("${custom.backend.endpoint.url}") String appUrl, List<T> dato
                         ,InputStream reportStream,Map<String, Object> params, com.vaadin.flow.component.UI ui){
        this.appUrl = appUrl;
        this.translatorService = translatorService;

        String tituloPrint=translatorService.getTranslatedMessage("titulodatoprint", getLocale(), null);
        H3 formTitle = new H3(tituloPrint);
        add(formTitle);

        String tooltipbotonpdf=translatorService.getTranslatedMessage("tooltipexportarpdf", getLocale(), null);
        String tooltipbotonword=translatorService.getTranslatedMessage("tooltipexportarword", getLocale(), null);
        String tooltipbotonexcel=translatorService.getTranslatedMessage("tooltipexportarexcel", getLocale(), null);
        String tooltipbotoncvs=translatorService.getTranslatedMessage("tooltipexportarcvs", getLocale(), null);
        String tooltipbotonsalida=translatorService.getTranslatedMessage("tooltipexportarsalida", getLocale(), null);

        Button pdfButton = new Button(new Icon(VaadinIcon.ADOBE_FLASH));
        pdfButton.setTooltipText(tooltipbotonpdf).withPosition(TooltipPosition.END);
        pdfButton.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
        Button wordButton = new Button(new Icon(VaadinIcon.ADD_DOCK));
        wordButton.setTooltipText(tooltipbotonword).withPosition(TooltipPosition.END);
        wordButton.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
        Button excelButton = new Button(new Icon(VaadinIcon.EXCHANGE));
        excelButton.setTooltipText(tooltipbotonexcel).withPosition(TooltipPosition.END);
        excelButton.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
        Button cvsButton = new Button(new Icon(VaadinIcon.CHEVRON_DOWN_SMALL));
        cvsButton.setTooltipText(tooltipbotoncvs).withPosition(TooltipPosition.END);
        cvsButton.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
        Button exitButton = new Button(new Icon(VaadinIcon.EXIT));
        exitButton.setTooltipText(tooltipbotonsalida).withPosition(TooltipPosition.END);
        exitButton.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
        HorizontalLayout buttonLayout = new HorizontalLayout(); 
        buttonLayout.addClassName("toolbar-horizontal-layout");

        pdfButton.addClickListener(e -> exportarPDF(dato,reportStream,params,ui));

        buttonLayout.add(pdfButton,wordButton,excelButton,cvsButton,exitButton);
        add(buttonLayout);
    }

    private <T> void exportarPDF(List<T> dato,InputStream reportStream,Map<String, Object> params,com.vaadin.flow.component.UI ui){
        ExportadorJasper exportadorJasper = new ExportadorJasper();
        String fileName = "output_" + UUID.randomUUID().toString() + ".pdf";
        exportadorJasper.exportarFormatoPDF(dato, reportStream,params,fileName);
        // Crear un recurso de transmisión (stream resource) para el archivo PDF
        StreamResource resource = new StreamResource(fileName, () -> {
        try {
            return new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        });
        if (ui != null) {
            String downloadUrl = "/api/download?fileName=" + fileName;
            ui.getPage().setLocation(downloadUrl);
        }
    }    
}