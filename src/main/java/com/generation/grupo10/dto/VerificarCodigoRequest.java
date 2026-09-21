package com.generation.grupo10.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificarCodigoRequest {

    private String email;
    private String codigo;
}