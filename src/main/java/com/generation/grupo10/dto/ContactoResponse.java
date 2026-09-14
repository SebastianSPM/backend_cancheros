package com.generation.grupo10.dto;




import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactoResponse {

    private Integer id;
    private String nombre;
    private String email;
    private String telefono;
    private String mensaje;
    private LocalDateTime fechaEnvio;
}