package com.cipres.application.control;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;

import org.springframework.stereotype.Component;

import com.vaadin.flow.i18n.I18NProvider;


@Component
public class Translator implements I18NProvider{


    private static final String BUNDLE_PREFIX = "messages";

    private static final List<Locale> locales = Collections
            .unmodifiableList(Arrays.asList(new Locale("en", "GB"), new Locale("fr", "FR")));
    
   
    @Override
    public List<Locale> getProvidedLocales() {
        return locales;
    }

    @Override
    public String getTranslation(String key, Locale locale, Object... params) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(locale);

        final ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_PREFIX, locale);

        String value;
        try {
            value = bundle.getString(key);
        } catch (final MissingResourceException e) {

            return "!" + locale.getLanguage() + ": " + key;
        }
        if (params.length > 0) {
            value = MessageFormat.format(value, params);
        }
        return value;
    }
    
}
