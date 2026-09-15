package com.generation.grupo10.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestablecerPasswordRequest {

    private String email;

    private String codigo;

    private String nuevaPassword;

}