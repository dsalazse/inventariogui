package com.cipres.application.control;


import com.vaadin.flow.i18n.I18NProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@Configuration
public class VaadinConfig {
    @Bean
    public I18NProvider i18nProvider() {
        return new MyI18NProvider();
    }

    private static class MyI18NProvider implements I18NProvider {

        private static final String BASE_NAME = "messages";
        private static final List<Locale> SUPPORTED_LOCALES = Arrays.asList(
                Locale.ENGLISH,
                new Locale("es"),
                Locale.FRENCH
        );

        @Override
        public List<Locale> getProvidedLocales() {
            return SUPPORTED_LOCALES;
        }

        @Override
        public String getTranslation(String key, Locale locale, Object... params) {
            ResourceBundle bundle = ResourceBundle.getBundle(BASE_NAME, locale);
            return bundle.getString(key);
        }
    }    
}
