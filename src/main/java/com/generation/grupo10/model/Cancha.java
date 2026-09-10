package com.generation.grupo10.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "canchas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cancha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    public Double getPrecioHora() {
        return precioPorHora.doubleValue();
    }

    public String getNombre() {
        return nombreCancha;
    }
}