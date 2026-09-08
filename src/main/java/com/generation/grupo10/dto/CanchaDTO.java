package com.generation.grupo10.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CanchaDTO {

    private Long id;
    private String nombreCancha;
    private String ubicacion;
    private String descripcion;
    private BigDecimal precioPorHora;
    private String tipo;
    private BigDecimal rating;
    private Integer totalResenas;
    private String imagenUrl;
    private Boolean disponible;
}