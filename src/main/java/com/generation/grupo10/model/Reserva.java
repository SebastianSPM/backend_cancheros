package com.generation.grupo10.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "reservas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Usuario que realiza la reserva
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Cancha reservada
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancha_id", nullable = false)
    private Cancha cancha;

    // Fecha de la reserva
    @Column(nullable = false)
    private LocalDate fecha;

    // Hora de inicio
    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    // Cantidad de horas reservadas
    @Column(name = "duracion_horas", nullable = false)
    private Integer duracion;

    // Precio de la cancha en el momento de reservar
    @Column(name = "precio_hora", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioHora;

    // Total de la reserva
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    // Estado
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoReserva estado;
}