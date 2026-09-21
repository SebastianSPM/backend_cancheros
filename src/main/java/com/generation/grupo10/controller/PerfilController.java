package com.generation.grupo10.controller;

import com.generation.grupo10.dto.EditarPerfilRequest;
import com.generation.grupo10.dto.UsuarioDTO;
import com.generation.grupo10.service.PerfilService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.generation.grupo10.dto.CambiarCorreoRequest;
import com.generation.grupo10.dto.VerificarCambioCorreoRequest;

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

    // Solicitar cambio de correo

    @PostMapping("/cambiar-correo/solicitar")
    public ResponseEntity<?> solicitarCambioCorreo(
            @RequestBody CambiarCorreoRequest request,
            Authentication authentication) {

        try {

            String emailActual =
                    authentication.getName();

            perfilService.solicitarCambioCorreo(
                    emailActual,
                    request.getNuevoCorreo()
            );

            return ResponseEntity.ok(
                    "Código enviado al nuevo correo"
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    // Verificar cambio de correo

    @PostMapping("/cambiar-correo/verificar")
    public ResponseEntity<?> verificarCambioCorreo(
            @RequestBody VerificarCambioCorreoRequest request,
            Authentication authentication) {

        try {

            String emailActual =
                    authentication.getName();

            return ResponseEntity.ok(
                    perfilService.verificarCambioCorreo(
                            emailActual,
                            request.getNuevoCorreo(),
                            request.getCodigo()
                    )
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}