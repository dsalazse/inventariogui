package com.cipres.application.views.error;

import org.springframework.context.annotation.PropertySource;

import com.cipres.application.control.TranslatorService;
import com.cipres.application.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Error")
@Route(value = "errorgeneral", layout = MainLayout.class)
@PropertySource("classpath:application-custom.properties")
public class ErrorUrlView extends Composite<Div>{
    private final TranslatorService translatorService;

    public ErrorUrlView(TranslatorService translatorService) {
        this.translatorService = translatorService;

        String errorGeneral = translatorService.getTranslatedMessage("errorgeneral", getLocale(), null);
        getContent().add(errorGeneral); 
    }
}
