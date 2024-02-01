package com.cipres.application.views.tablas;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.vaadin.crudui.crud.impl.GridCrud;


import com.cipres.application.control.TranslatorService;
import com.cipres.application.model.Grupo;
import com.cipres.application.service.GenericRestClientService;
import com.cipres.application.views.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import lombok.extern.log4j.Log4j2;
@PageTitle("Grupo Crud")
@Route(value = "grupo", layout = MainLayout.class)
@PropertySource("classpath:application-custom.properties")
@Log4j2
public class GrupoView extends VerticalLayout{
    @Value("${custom.backend.endpoint.url}")
    private String appUrl;
    private static String endPoint="grupo";

    private final TranslatorService translatorService;
    private final GenericRestClientService grupoService;

    public GrupoView(@Value("${custom.backend.endpoint.url}") String appUrl, TranslatorService translatorService, GenericRestClientService grupoService){
        this.appUrl = appUrl;
        this.translatorService = translatorService;
        this.grupoService = grupoService;
        loadGrid();
    }


    private void loadGrid(){
            GridCrud<Grupo> crud = new GridCrud<>(Grupo.class);
            //Traduccion
            String columnId=translatorService.getTranslatedMessage("tituloid", getLocale(), null); 
            String columnDesc=translatorService.getTranslatedMessage("titulodescripcion", getLocale(), null); 
            String columnCompany=translatorService.getTranslatedMessage("titulocompania", getLocale(), null);

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
                () -> grupoService.findAll(Grupo[].class,endPoint),
                grupo -> grupoService.save(grupo,Grupo.class,endPoint),
                grupo -> {
                    if (grupo.getId().equals(0L)){
                        try {
                            throw new Exception("pailas");
                        } catch (Exception e) {
                            log.info("Se presento error: " + e.getMessage());
                        }
                    }
                    return grupoService.save(grupo,Grupo.class,endPoint);
                },
                grupo -> grupoService.delete(grupo.getId(),endPoint)
            );
    }        
    
}
