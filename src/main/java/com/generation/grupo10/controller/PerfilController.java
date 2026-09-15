package com.generation.grupo10.controller;

import com.generation.grupo10.dto.UsuarioDTO;
import com.generation.grupo10.model.Usuario;
import com.generation.grupo10.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/perfil")
@RequiredArgsConstructor
public class PerfilController {

    private final UsuarioRepository usuarioRepository;


    //Ver perfil

    @GetMapping
    public ResponseEntity<UsuarioDTO> obtenerPerfil(
            Authentication authentication) {

        String email = authentication.getName();

        return usuarioRepository.findByEmail(email)
                .map(usuario -> ResponseEntity.ok(
                        convertirDTO(usuario)
                ))
                .orElse(ResponseEntity.notFound().build());
    }

    //Convertir a dto
    private UsuarioDTO convertirDTO(Usuario usuario) {

        return new UsuarioDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getTelefono(),
                usuario.getFotoPerfil(),
                usuario.getRol(),
                usuario.getFechaCreacion()
        );
    }
}