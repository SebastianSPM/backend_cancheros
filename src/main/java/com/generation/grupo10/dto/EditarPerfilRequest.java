package com.generation.grupo10.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditarPerfilRequest {

    private String nombre;
    private String apellido;
    private String telefono;
    private String token;
}