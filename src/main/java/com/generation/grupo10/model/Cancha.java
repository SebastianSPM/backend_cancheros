package com.generation.grupo10.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "canchas")
public class Cancha {
    public Cancha() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre_cancha", nullable = false, length = 150)
    private String nombreCancha;

    @Column(nullable = false, length = 255)
    private String ubicacion;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "precio_por_hora", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioPorHora;

    @Column(nullable = false, length = 50)
    private String tipo;

    @Column(precision = 2, scale = 1)
    private BigDecimal rating;

    @Column(name = "total_resenas")
    private Integer totalResenas;

    @Column(name = "imagen_url")
    private String imagenUrl;

    private Boolean disponible;
}