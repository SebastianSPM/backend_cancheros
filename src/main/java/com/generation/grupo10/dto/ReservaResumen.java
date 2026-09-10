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
public class ReservaResumen {

    private Long id;
    private String nombreCancha;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private Integer duracion;
    private Double total;
    private EstadoReserva estado;
}