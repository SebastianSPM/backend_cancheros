package com.generation.grupo10.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificarCambioCorreoRequest {

    private String nuevoCorreo;
    private String codigo;
}