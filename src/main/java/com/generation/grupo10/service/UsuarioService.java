package com.generation.grupo10.service;

import com.generation.grupo10.dto.UsuarioDTO;
import com.generation.grupo10.model.Usuario;
import com.generation.grupo10.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> actualizarUsuario(
            Long id,
            UsuarioDTO dto) {

        return usuarioRepository.findById(id)
                .map(usuario -> {

                    usuario.setNombre(dto.getNombre());
                    usuario.setApellido(dto.getApellido());
                    usuario.setEmail(dto.getEmail());
                    usuario.setTelefono(dto.getTelefono());
                    usuario.setRol(dto.getRol());

                    return usuarioRepository.save(usuario);
                });
    }

    public boolean eliminarUsuario(Long id) {

        if (!usuarioRepository.existsById(id)) {
            return false;
        }

        usuarioRepository.deleteById(id);
        return true;
    }
}