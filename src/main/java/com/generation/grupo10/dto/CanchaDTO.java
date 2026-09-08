package com.generation.grupo10.dto;

import java.math.BigDecimal;

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

    public CanchaDTO() {
    }

    public CanchaDTO(Long id, String nombreCancha, String ubicacion,
                     String descripcion, BigDecimal precioPorHora,
                     String tipo, BigDecimal rating, Integer totalResenas,
                     String imagenUrl, Boolean disponible) {

        this.id = id;
        this.nombreCancha = nombreCancha;
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
        this.precioPorHora = precioPorHora;
        this.tipo = tipo;
        this.rating = rating;
        this.totalResenas = totalResenas;
        this.imagenUrl = imagenUrl;
        this.disponible = disponible;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCancha() {
        return nombreCancha;
    }

    public void setNombreCancha(String nombreCancha) {
        this.nombreCancha = nombreCancha;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecioPorHora() {
        return precioPorHora;
    }

    public void setPrecioPorHora(BigDecimal precioPorHora) {
        this.precioPorHora = precioPorHora;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public Integer getTotalResenas() {
        return totalResenas;
    }

    public void setTotalResenas(Integer totalResenas) {
        this.totalResenas = totalResenas;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }
}