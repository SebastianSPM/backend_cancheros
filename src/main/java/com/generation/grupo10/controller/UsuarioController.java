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
                .map(usuario -> new UsuarioDTO(
                        usuario.getId(),
                        usuario.getNombre(),
                        usuario.getApellido(),
                        usuario.getEmail(),
                        usuario.getTelefono(),
                        usuario.getFotoPerfil(),
                        usuario.getRol(),
                        usuario.getFechaCreacion()
                ))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable Long id) {

        return usuarioService.buscarPorId(id)
                .map(usuario -> {

                    UsuarioDTO dto = new UsuarioDTO(
                            usuario.getId(),
                            usuario.getNombre(),
                            usuario.getApellido(),
                            usuario.getEmail(),
                            usuario.getTelefono(),
                            usuario.getFotoPerfil(),
                            usuario.getRol(),
                            usuario.getFechaCreacion()
                    );

                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Usuario guardarUsuario(@RequestBody Usuario usuario) {
        return usuarioService.guardarUsuario(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(
            @PathVariable Long id,
            @RequestBody Usuario usuario) {

        return usuarioService.buscarPorId(id)
                .map(usuarioExistente -> {

                    usuarioExistente.setNombre(usuario.getNombre());
                    usuarioExistente.setApellido(usuario.getApellido());
                    usuarioExistente.setEmail(usuario.getEmail());
                    usuarioExistente.setTelefono(usuario.getTelefono());
                    usuarioExistente.setPassword(usuario.getPassword());
                    usuarioExistente.setFotoPerfil(usuario.getFotoPerfil());
                    usuarioExistente.setRol(usuario.getRol());

                    return ResponseEntity.ok(
                            usuarioService.guardarUsuario(usuarioExistente)
                    );
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}