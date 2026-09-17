package com.generation.grupo10.controller;

import com.generation.grupo10.dto.UsuarioDTO;
import com.generation.grupo10.model.Usuario;
import com.generation.grupo10.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<UsuarioDTO> listarUsuarios() {

        return usuarioService.listarUsuarios()
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarPorId(
            @PathVariable Long id) {

        return usuarioService.buscarPorId(id)
                .map(usuario ->
                        ResponseEntity.ok(convertirDTO(usuario))
                )
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(
            @PathVariable Long id,
            @RequestBody UsuarioDTO dto) {

        return usuarioService.actualizarUsuario(id, dto)
                .map(usuario ->
                        ResponseEntity.ok(convertirDTO(usuario))
                )
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(
            @PathVariable Long id) {

        if (!usuarioService.eliminarUsuario(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

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