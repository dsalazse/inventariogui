package com.cipres.application.control;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application-custom.properties")
public class ConfigUrlEndPoint {
   
    @Value("${custom.backend.endpoint.url}")
    private String appUrl;

    public String getAppUrl() {
        return appUrl;
    }
   
    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }    
}
