package com.generation.grupo10.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisponibilidadResponse {

    private LocalTime horaInicio;

    private LocalTime horaFin;

    private Boolean disponible;
}