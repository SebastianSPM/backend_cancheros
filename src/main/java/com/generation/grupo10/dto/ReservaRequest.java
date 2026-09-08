package com.generation.grupo10.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaRequest {

    private Long canchaId;

    private String nombreCompleto;

    private String correo;

    private String telefono;

    private LocalDate fecha;

    private LocalTime horaInicio;

    private Integer duracion;
}