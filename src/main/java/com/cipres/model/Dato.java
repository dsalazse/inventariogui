package com.cipres.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Dato {
    private Long id;
    private String descripcion;
    private Long compania;

    @JsonCreator
    public Dato(@JsonProperty("id") Long id, 
                @JsonProperty("descripcion") String descripcion, 
                @JsonProperty("compania") Long compania) {
        this.id = id;
        this.descripcion = descripcion;
        this.compania = compania;
    }

    @JsonGetter("id")
    public Long getId() {
        return id;
    }
    @JsonSetter("id")
    public void setId(Long id) {
        this.id = id;
    }

    @JsonGetter("compania")
    public Long getCompania() {
        return compania;
    }
    @JsonSetter("compania")
    public void setCompania(Long compania) {
        this.compania = compania;
    }


    @JsonGetter("descripcion")
    public String getDescripcion() {
        return descripcion;
    }
    @JsonSetter("descripcion")
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}