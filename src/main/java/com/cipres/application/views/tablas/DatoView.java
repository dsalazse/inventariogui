package com.cipres.application.views.tablas;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

import com.cipres.application.control.TranslatorService;
import com.cipres.application.control.ValidationMessage;
import com.cipres.application.views.MainLayout;
import com.cipres.application.views.imprimir.DatoPrint;
import com.cipres.model.Dato;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.shared.Tooltip.TooltipPosition;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import lombok.extern.log4j.Log4j2;



@PageTitle("Dato Crud")
@Route(value = "dato", layout = MainLayout.class)
@PropertySource("classpath:application-custom.properties")
@Log4j2
public class DatoView extends VerticalLayout {
        
    @Value("${custom.backend.endpoint.url}")
    private String appUrl;

    private final TranslatorService translatorService;

    private Binder<Dato> binder = new Binder<>(Dato.class);
    private List<Dato>  datos = new ArrayList<>();   
    Grid<Dato> grid = new Grid<>(Dato.class, false); 
    
    public DatoView(@Value("${custom.backend.endpoint.url}") String appUrl, TranslatorService translatorService) {
        this.appUrl = appUrl;
        this.translatorService = translatorService;
        loadGrid();
    }

    /**
     * 
     */
    private void loadGrid(){
        String columnId=translatorService.getTranslatedMessage("tituloid", getLocale(), null); 
        String columnDesc=translatorService.getTranslatedMessage("titulodescripcion", getLocale(), null); 
        String columnCompany=translatorService.getTranslatedMessage("titulocompania", getLocale(), null);
        String tooltipbotonNuevo=translatorService.getTranslatedMessage("tooltipnuevo", getLocale(), null);
        String tooltipimprimir=translatorService.getTranslatedMessage("tooltipimprimir", getLocale(), null);
        String botonNuevo=translatorService.getTranslatedMessage("titulonuevo", getLocale(), null);
        String botonEditar=translatorService.getTranslatedMessage("tituloeditar", getLocale(), null);
        String botonBorrar=translatorService.getTranslatedMessage("tituloborrar", getLocale(), null); 
        String campoEsRequerido=translatorService.getTranslatedMessage("campoesrequerido", getLocale(), null); 
        String campoEsNumerico=translatorService.getTranslatedMessage("campodebesernumero", getLocale(), null); 
        String cabeceraborrado=translatorService.getTranslatedMessage("cabeceraborrado", getLocale(), null); 
        String textoborrado=translatorService.getTranslatedMessage("textoborrado", getLocale(), null);
        String confirmaborrado=translatorService.getTranslatedMessage("confirmaborrado", getLocale(), null);
        ValidationMessage idValidationMessage = new ValidationMessage();
        ValidationMessage descriptionValidationMessage = new ValidationMessage();
        ValidationMessage companiaValidationMessage = new ValidationMessage();
        

        
        Editor<Dato> editor = grid.getEditor();

        Grid.Column<Dato> idColumn = grid
                .addColumn(Dato::getId).setHeader(columnId)
                .setWidth("5%").setFlexGrow(0)
                .setSortable(true);
        Grid.Column<Dato> descriptionColumn = grid
                .addColumn(Dato::getDescripcion).setHeader(columnDesc)
                .setWidth("75%").setFlexGrow(0)
                .setSortable(true);
        Grid.Column<Dato> companyColumn = grid
                .addColumn(Dato::getCompania).setHeader(columnCompany)
                .setWidth("5%").setFlexGrow(0)
                .setSortable(true);
        Grid.Column<Dato> editColumn = grid.addComponentColumn(dato -> {
            Button editButton = new Button();
            Icon icon = new Icon(VaadinIcon.EDIT);
            editButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);
            editButton.setIcon(icon); 

            editButton.addClickListener(e -> {
                if (editor.isOpen())
                    editor.cancel();
                grid.getEditor().editItem(dato);
            });
            return editButton;
        }).setWidth("10%").setFlexGrow(0);
        
        Grid.Column<Dato> deleteColumn = grid.addComponentColumn(datoborrar -> {
            Button deleteButton = new Button();
            Icon icon = new Icon(VaadinIcon.ERASER);
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);
            deleteButton.setIcon(icon); 
            deleteButton.addClickListener(e -> {
                ConfirmDialog dialog = new ConfirmDialog(
                        cabeceraborrado,
                        textoborrado,
                        confirmaborrado, 

                        confirm -> {

                        if (confirm.equals(true)) {
                                
                                
                        }
                        });
                dialog.open();
        });
        return deleteButton;
        }).setWidth("5%").setFlexGrow(0);



        
        editor.setBinder(binder);
        editor.setBuffered(true);

        TextField textFieldId = new TextField();
        textFieldId.setWidthFull();
        binder.forField(textFieldId)
                .asRequired(campoEsRequerido)
                .withStatusLabel(idValidationMessage)
                .withNullRepresentation("")
                .withConverter(new StringToLongConverter (campoEsNumerico))
                .bind(Dato::getId, Dato::setId);
        idColumn.setEditorComponent(textFieldId);

        TextField textFieldDesc = new TextField();
        textFieldDesc.setWidthFull();
        binder.forField(textFieldDesc).asRequired(campoEsRequerido)
                .withStatusLabel(descriptionValidationMessage)
                .withNullRepresentation("")
                .bind(Dato::getDescripcion, Dato::setDescripcion);
        descriptionColumn.setEditorComponent(textFieldDesc);

        TextField textFieldCompany = new TextField();
        textFieldCompany.setWidthFull();
        binder.forField(textFieldCompany).asRequired(campoEsRequerido)
                .withStatusLabel(companiaValidationMessage)
                .withNullRepresentation("")
                .withConverter(new StringToLongConverter(campoEsNumerico))
                .bind(Dato::getCompania, Dato::setCompania);
        companyColumn.setEditorComponent(textFieldCompany);

        Button saveButton = new Button("Save", e -> editor.save());
        Button cancelButton = new Button(VaadinIcon.CLOSE.create(),
                e -> editor.cancel());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ICON,
                ButtonVariant.LUMO_ERROR);
        HorizontalLayout actions = new HorizontalLayout(saveButton,
                cancelButton);
        actions.setPadding(false);
        editColumn.setEditorComponent(actions);

        editor.addCancelListener(e -> {
            idValidationMessage.setText("");
            descriptionValidationMessage.setText("");
            companiaValidationMessage.setText("");
        });

        List<Dato> dato = loadDataFromUrl();
        Dialog editDialog = new Dialog();
        FormLayout formLayout = new FormLayout();



        if (dato.size()>0){
            grid.setItems(dato);
            grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, 
                                  GridVariant.LUMO_COMPACT,
                                  GridVariant.LUMO_ROW_STRIPES,
                                  GridVariant.LUMO_WRAP_CELL_CONTENT);
            TextField fieldIdFL = new TextField(columnId);
            TextField fieldDescFL = new TextField(columnDesc);
            TextField fieldCompanyFL = new TextField(columnCompany);
            Button saveButtonFL = new Button("Guardar");
            Button cancelButtonFL = new Button("Cancelar");
            saveButton.addClickListener(e -> saveRecord());
            cancelButton.addClickListener(e -> closeForm());

            
            formLayout.add(fieldIdFL,fieldDescFL,fieldCompanyFL,saveButtonFL,cancelButtonFL);
            editDialog.add(formLayout);
            
            Button addButton = new Button(new Icon(VaadinIcon.PLUS));
            addButton.setTooltipText(tooltipbotonNuevo).withPosition(TooltipPosition.END);
            addButton.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
            addButton.addClickShortcut(Key.KEY_N, KeyModifier.CONTROL, KeyModifier.ALT);
            addButton.addClickListener(e -> openPopup());
            Button printButton = new Button(new Icon(VaadinIcon.PRINT));
            printButton.setTooltipText(tooltipimprimir).withPosition(TooltipPosition.END);
            printButton.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
            printButton.addClickShortcut(Key.KEY_P, KeyModifier.ALT);
            printButton.addClickListener(e -> openPopupPrint(dato));
            

            Dialog dialog = new Dialog(); 
            

            Button closeButton = new Button(new Icon(VaadinIcon.CLOSE_SMALL)); 
            closeButton.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
            closeButton.addClickListener(e -> {
                dialog.close(); 
            });

            
            HorizontalLayout buttonLayout = new HorizontalLayout();
            buttonLayout.add(addButton, printButton,closeButton);


            getThemeList().clear();
            getThemeList().add("spacing-s");

            add(buttonLayout);
            add(grid, idValidationMessage, descriptionValidationMessage,
                        companiaValidationMessage);
        } else{
            String errorGeneral = translatorService.getTranslatedMessage("errorgeneral", getLocale(), null);
            add(errorGeneral);     
            UI.getCurrent().navigate("errorgeneral");    
        }               
    }

    private List<Dato> loadDataFromUrl() {        
        String url  = appUrl + "dato";
        Dato[] datos=null;

        RestTemplate restTemplate = new RestTemplate();
        try{
           datos = restTemplate.getForObject(url, Dato[].class);
        }catch (Exception e) {
           log.error(e.getMessage());
           datos = new Dato[0];
        }
        return new ArrayList<>(Arrays.asList(datos));
    }

    private Button createSaveButton() {
        String botonGuardar=translatorService.getTranslatedMessage("tituloguardar", getLocale(), null); 

        return new Button(botonGuardar, event -> {
            saveRecord();
        });
    }


    private void saveRecord() {
        Dato editedRecord = binder.getBean();
        if (editedRecord != null) {
            if (!datos.contains(editedRecord)) {
                datos.add(editedRecord);
                Notification.show("Registro agregado correctamente");
            } else {
                Notification.show("Registro actualizado correctamente");
            }
            grid.getDataProvider().refreshAll();
            binder.setBean(null);
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

    private void openPopup() {
        Dialog dialog = new Dialog();
        dialog.setWidth("25%");
        dialog.setHeight("40%"); 

        MyFormLayout myFormLayout = new MyFormLayout(translatorService,appUrl);

        dialog.add(myFormLayout);

        // Abre el diálogo
        dialog.open();
    }

    private void openPopupPrint(List<Dato> dato){
        Dialog dialog = new Dialog();
        dialog.setWidth("25%");
        dialog.setHeight("40%");
        InputStream reportStream = getClass().getResourceAsStream("/reportes/dato.jasper");
        Map<String, Object> params = new HashMap<>();

        if (reportStream!=null){
            UI ui = UI.getCurrent();
            DatoPrint myDatoPrint = new DatoPrint(translatorService,appUrl,dato,reportStream,params,ui);
            dialog.add(myDatoPrint);
            dialog.open();
        }
    }
}
