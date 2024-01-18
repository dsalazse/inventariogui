package com.cipres.application.control;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.DefaultErrorHandler;
import com.vaadin.flow.server.ErrorEvent;
import lombok.extern.log4j.Log4j2;

/**
 * Esta clase representa un controlador personalizado para manejar errores en la aplicación.
 * Extiende DefaultErrorHandler de Vaadin y proporciona un manejo personalizado de los errores.
 */
 
@Log4j2
public class ControladorError extends DefaultErrorHandler {


    /**
     * Este método se llama cuando se produce un error en la aplicación.
     * Proporciona un manejo personalizado de los errores, registrando el error y
     * redirigiendo al usuario a una página de error general.
     *
     * @param event El evento de error que contiene información sobre el error.
     */
    @Override
    public void error(ErrorEvent event) {
        Throwable error = findRelevantThrowable(event.getThrowable());
        log.error(error.getMessage());
        UI.getCurrent().navigate("errorgeneral");
    }
}