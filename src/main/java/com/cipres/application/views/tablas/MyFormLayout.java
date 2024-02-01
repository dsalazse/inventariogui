package com.cipres.application.views.tablas;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.cipres.application.control.TranslatorService;
import com.cipres.application.control.ValidationMessage;
import com.cipres.application.model.Dato;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.log4j.Log4j2;

@SpringComponent
@UIScope
@PropertySource("classpath:application-custom.properties")
@Log4j2
public class MyFormLayout extends FormLayout {
    @Value("${custom.backend.endpoint.url}")
    private String appUrl;

    private final TextField idField = new TextField("ID");
    private final TextField companyField = new TextField("Compañía");
    private final TextField descriptionField = new TextField("Descripción");

    private final Button saveButton = new Button("Guardar");
    private final Button cancelButton = new Button("Cancelar");

    private final Binder<Dato> binder = new Binder<>(Dato.class);
    private final TranslatorService translatorService;

    public MyFormLayout(TranslatorService translatorService, @Value("${custom.backend.endpoint.url}") String appUrl) {
        this.translatorService = translatorService;
        this.appUrl = appUrl;



        String campoEsRequerido=translatorService.getTranslatedMessage("campoesrequerido", getLocale(), null); 
        String campoEsNumerico=translatorService.getTranslatedMessage("campodebesernumero", getLocale(), null);
        String tituloSave=translatorService.getTranslatedMessage("titulodatosave", getLocale(), null);
        ValidationMessage idValidationMessage = new ValidationMessage();
        ValidationMessage descriptionValidationMessage = new ValidationMessage();
        ValidationMessage companiaValidationMessage = new ValidationMessage();
        
        H3 formTitle = new H3(tituloSave);
        add(formTitle);

        binder.forField(idField)
                .asRequired(campoEsRequerido)
                .withStatusLabel(idValidationMessage)
                .withNullRepresentation("")
                .withConverter(new StringToLongConverter(campoEsNumerico))
                .bind(Dato::getId, Dato::setId);
        binder.forField(descriptionField).asRequired(campoEsRequerido)
                .withStatusLabel(descriptionValidationMessage)
                .withNullRepresentation("")
                .bind(Dato::getDescripcion, Dato::setDescripcion); 
        binder.forField(companyField).asRequired(campoEsRequerido)
                .withStatusLabel(companiaValidationMessage)
                .withNullRepresentation("")
                .withConverter(new StringToLongConverter(campoEsNumerico))
                .bind(Dato::getCompania, Dato::setCompania);
        add(idField, companyField, descriptionField);

        HorizontalLayout buttonLayout = new HorizontalLayout(); 
        // Configurar acciones de botón
        saveButton.addClickListener(e -> save());
        cancelButton.addClickListener(e -> closeForm());
        buttonLayout.add(saveButton, cancelButton);

        // Agregar campos y botones al diseño
        add(buttonLayout);
    }

    public void setBean(Dato bean) {
        binder.setBean(bean);
    }

    private void save() {
        try {
            Dato bean = new Dato();
            binder.bindInstanceFields(this);
            binder.writeBean(bean);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Construir el objeto JSON que se enviará en el cuerpo de la solicitud
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonBody = objectMapper.writeValueAsString(bean);

            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);


            String url  = appUrl + "dato";
            
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class
            );

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                Notification.show("Datos ok");
            } else {
                Notification.show("Error al enviar datos");
            }
            binder.readBean(new Dato());
        } catch (Exception e) {
            log.error(e.getMessage() + " " + e.getCause() + " " + e.getStackTrace());
            String errorGuardar = translatorService.getTranslatedMessage("errorguardar", getLocale(), null);
            
            add(errorGuardar + ". "  );     
        }
    }

    private void closeForm() {
        // Cerrar el formulario o realizar acciones de cancelación
        getUI().ifPresent(ui -> {
            Dialog dialog = (Dialog) getParent().orElse(null);
            if (dialog != null) {
                dialog.close();
            }
        });
    }
}