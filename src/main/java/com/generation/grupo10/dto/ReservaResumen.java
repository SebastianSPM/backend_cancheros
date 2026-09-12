package com.generation.grupo10.dto;

import com.generation.grupo10.model.EstadoReserva;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaResumen {

    private Long id;

    private String nombreCancha;

    private LocalDate fecha;

    private LocalTime horaInicio;

    private LocalTime horaFin;

    private Integer duracion;

    private BigDecimal total;

    private EstadoReserva estado;
}