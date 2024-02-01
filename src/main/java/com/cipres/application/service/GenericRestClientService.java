package com.cipres.application.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;


@Service
@Log4j2
public class GenericRestClientService {
    @Value("${custom.backend.endpoint.url}")
    private String appUrl;

    public GenericRestClientService(@Value("${custom.backend.endpoint.url}") String appUrl) {
        this.appUrl = appUrl;
    }


    public <T> List<T>   findAll(Class<T[]> classObject, String endPoint) {
        String apiUrl = appUrl + endPoint;
        List<T> lista = null;
        T[] datos= null; 
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);            

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                datos = convertJSONToObject(responseEntity.getBody(),classObject);
                log.info("Este es el valor devuelto por <T> List<T>: " + datos);
                log.info("Este es el valor devuelto por List<T> lista: " + lista);
                return new ArrayList<>(Arrays.asList(datos));
            } else {
                log.error("Error al intentar obtener datos de: " + apiUrl);
                return new ArrayList<>();
            }
        } catch (Exception e) {
            log.error("Error: " + e.getMessage() + " al intentar obtener datos de: " + apiUrl);
            return new ArrayList<>();
        }
        
    }



    public <T> T save(T object, Class<T> classObject, String endPoint) {
        String url = appUrl + endPoint;
        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        String jsonBody = convertObjectToJSON(object);
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);   
        
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                log.info("Datos correctamente almacenados: " + jsonBody);
                return object;
            } else {
                log.error("Se presentó un error al intentar almacenar los datos: " + jsonBody);
                return null;
            }
        } catch (Exception e) {
            log.error("Error al realizar la solicitud: " + e.getMessage());
            return null; 
        }
    }

    public void delete(Long id, String endPoint) {
        String apiUrl = appUrl + endPoint + "/{id}";
        RestTemplate restTemplate = new RestTemplate();
    
        try {
            restTemplate.delete(apiUrl, id);
        } catch (Exception e) {
            log.error("Error al realizar la solicitud de borrado: " + apiUrl + ".  Error: " +  e.getMessage());
        }
    }

    public long count(String endPoint) {
        String apiUrl = appUrl + endPoint + "/count";
        RestTemplate restTemplate = new RestTemplate();
    
        try {
            return restTemplate.getForObject(apiUrl, Long.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0;
        }
    }


    private static String convertObjectToJSON(Object objeto) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(objeto);
        } catch (Exception e) {
            log.error("Error al convertir el objeto en json. " +  e.getMessage());
            return null;
        }
    }

    private <T> T[] convertJSONToObject(String json, Class<T[]> classObject) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, classObject);
        } catch (JsonProcessingException e) {
            log.error("Error al convertir json en objeto. " +  e.getMessage());
            return null;
        }
    }
}