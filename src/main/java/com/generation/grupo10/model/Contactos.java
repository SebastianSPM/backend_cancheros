package com.generation.grupo10.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
@Entity
@Table(name = "contactos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contactos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, length = 150)
    private String nombre;
    @Column(nullable = false, length = 150)
    private String email;
    @Column(nullable = false, length = 20)
    private String telefono;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String mensaje;
    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;
}
