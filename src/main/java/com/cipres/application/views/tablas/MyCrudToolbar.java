package com.cipres.application.views.tablas;

import com.cipres.application.control.TranslatorService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class MyCrudToolbar extends HorizontalLayout {
    private final TranslatorService translatorService;

    public MyCrudToolbar(TranslatorService translatorService) {
        this.translatorService = translatorService;
        // Botón de Agregar
        Button addButton = new Button("Agregar", new Icon(VaadinIcon.PLUS));
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // Botón de Editar
        Button editButton = new Button("Editar", new Icon(VaadinIcon.EDIT));
        editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // Botón de Eliminar
        Button deleteButton = new Button("Eliminar", new Icon(VaadinIcon.TRASH));
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        // Agregar botones al toolbar
        add(addButton, editButton, deleteButton);
    }
}

