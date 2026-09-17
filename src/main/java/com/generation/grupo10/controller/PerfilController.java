package com.generation.grupo10.controller;

import com.generation.grupo10.dto.EditarPerfilRequest;
import com.generation.grupo10.dto.UsuarioDTO;
import com.generation.grupo10.service.PerfilService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/perfil")
@RequiredArgsConstructor
public class PerfilController {

    private final PerfilService perfilService;

    // Ver perfil

    @GetMapping
    public ResponseEntity<?> obtenerPerfil(
            Authentication authentication) {

        try {

            String email = authentication.getName();

            return ResponseEntity.ok(
                    perfilService.obtenerPerfil(email)
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    // Editar perfil

    @PutMapping
    public ResponseEntity<?> editarPerfil(
            @RequestBody EditarPerfilRequest request,
            Authentication authentication) {

        try {

            String email = authentication.getName();

            return ResponseEntity.ok(
                    perfilService.editarPerfil(email, request)
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}