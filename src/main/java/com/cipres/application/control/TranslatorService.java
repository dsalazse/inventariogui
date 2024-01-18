package com.cipres.application.control;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.server.VaadinService;

@Service
public class TranslatorService {
    private final I18NProvider i18nProvider;
    
    @Autowired
    public TranslatorService(I18NProvider i18nProvider){
        this.i18nProvider = i18nProvider;
    }


    public static String tr(String key, Object... params) {
        return VaadinService.getCurrent().getInstantiator().getI18NProvider().getTranslation(key, UI.getCurrent().getLocale(), params);
    }

    public String getTranslatedMessage(String key, Locale locale, Object params) {
        return i18nProvider.getTranslation(key, locale, params);
    }

}