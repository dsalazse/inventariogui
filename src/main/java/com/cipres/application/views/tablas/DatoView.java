package com.cipres.application.views.tablas;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.vaadin.crudui.crud.impl.GridCrud;
import com.cipres.application.control.TranslatorService;
import com.cipres.application.model.Dato;
import com.cipres.application.service.GenericRestClientService;
import com.cipres.application.views.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
    private static String endPoint="dato";

    private final TranslatorService translatorService;
    private final GenericRestClientService datoService;   
    
    public DatoView(@Value("${custom.backend.endpoint.url}") String appUrl, TranslatorService translatorService, GenericRestClientService datoService) {
        this.appUrl = appUrl;
        this.translatorService = translatorService;
        this.datoService = datoService;
        loadGrid();
    }

    /**
     * 
     */
    private void loadGrid(){
        String columnId=translatorService.getTranslatedMessage("tituloid", getLocale(), null); 
        String columnDesc=translatorService.getTranslatedMessage("titulodescripcion", getLocale(), null); 
        String columnCompany=translatorService.getTranslatedMessage("titulocompania", getLocale(), null);

        GridCrud<Dato> crud = new GridCrud<>(Dato.class);


        // grid configuration
            crud.getGrid().setColumns("id", "descripcion", "compania");
            crud.getGrid().setPageSize(10);
            crud.getGrid().setColumnReorderingAllowed(true);

            // form configuration
            crud.getCrudFormFactory().setUseBeanValidation(true);

            crud.getCrudFormFactory().setVisibleProperties("id", "descripcion", "compania");
            crud.setRowCountCaption("Total registros: ");

             // layout configuration
            setSizeFull();
            add(crud);
            crud.setFindAllOperationVisible(false);

            // logic configuration
            //crud.setOperations(grupoService.findAll(), grupoService.save(null), grupoService.save(null), grupoService.delete(0L);
            crud.setOperations(
                () -> datoService.findAll(Dato[].class,endPoint),
                dato -> datoService.save(dato,Dato.class,endPoint),
                dato -> {
                    if (dato.getId().equals(0L)){
                        try {
                            throw new Exception("ID error no puede ser cero (0)");
                        } catch (Exception e) {
                            log.info("Se presento error: " + e.getMessage());
                        }
                    }
                    return datoService.save(dato,Dato.class,endPoint);
                },
                dato -> datoService.delete(dato.getId(),endPoint)
            );
    } 
}