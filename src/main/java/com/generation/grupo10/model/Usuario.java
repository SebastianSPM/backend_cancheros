package com.generation.grupo10.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false, length = 20)
    private String telefono;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "foto_perfil")
    private String fotoPerfil;

    @Column(length = 20)
    private String rol;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "correo_verificado", nullable = false)
    private boolean correoVerificado;

    // ==========================================
    // RECUPERACIÓN DE CONTRASEÑA
    // ==========================================

    @Column(name = "codigo_recuperacion", length = 6)
    private String codigoRecuperacion;

    @Column(name = "codigo_recuperacion_expiracion")
    private LocalDateTime codigoRecuperacionExpiracion;
}