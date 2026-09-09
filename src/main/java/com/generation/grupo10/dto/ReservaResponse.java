package com.generation.grupo10.dto;

import com.generation.grupo10.model.EstadoReserva;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaResponse {

    private Long id;

    private Long usuarioId;

    private Long canchaId;

    private String nombreCancha;

    private String nombreCompleto;

    private String correo;

    private String telefono;

    private LocalDate fecha;

    private LocalTime horaInicio;

    private LocalTime horaFin;

    private Integer duracion;

    private Double total;

    private EstadoReserva estado;
}