package com.generation.grupo10.service;

import com.generation.grupo10.dto.EditarPerfilRequest;
import com.generation.grupo10.dto.UsuarioDTO;
import com.generation.grupo10.enums.TokenPurpose;
import com.generation.grupo10.model.TokenRecuperacion;
import com.generation.grupo10.model.Usuario;
import com.generation.grupo10.repository.TokenRecuperacionRepository;
import com.generation.grupo10.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PerfilService {

    private final UsuarioRepository usuarioRepository;
    private final TokenRecuperacionRepository tokenRecuperacionRepository;
    private final CodigoVerificacionService codigoVerificacionService;

    public UsuarioDTO obtenerPerfil(String email) {

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Usuario no encontrado"
                        )
                );

        return convertirDTO(usuario);
    }

    public UsuarioDTO editarPerfil(
            String email,
            EditarPerfilRequest request) {

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Usuario no encontrado"
                        )
                );

        TokenRecuperacion token =
                tokenRecuperacionRepository
                        .findByTokenAndPurposeAndUsadoFalse(
                                request.getToken(),
                                TokenPurpose.PROFILE_EDIT
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Token inválido o ya utilizado"
                                )
                        );

        if (!token.getEmail().equals(usuario.getEmail())) {
            throw new IllegalArgumentException(
                    "El token no corresponde al usuario"
            );
        }

        if (token.getFechaExpiracion()
                .isBefore(LocalDateTime.now())) {

            throw new IllegalArgumentException(
                    "El enlace ha expirado"
            );
        }

        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setTelefono(request.getTelefono());

        usuarioRepository.save(usuario);

        token.setUsado(true);
        tokenRecuperacionRepository.save(token);

        return convertirDTO(usuario);
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


    public void solicitarCambioCorreo(
            String emailActual,
            String nuevoCorreo) {

        Usuario usuario = usuarioRepository
                .findByEmail(emailActual)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Usuario no encontrado"
                        )
                );

        if (nuevoCorreo == null || nuevoCorreo.isBlank()) {
            throw new IllegalArgumentException(
                    "El nuevo correo es obligatorio"
            );
        }

        if (usuario.getEmail().equalsIgnoreCase(nuevoCorreo)) {
            throw new IllegalArgumentException(
                    "El nuevo correo debe ser diferente al actual"
            );
        }

        if (usuarioRepository.findByEmail(nuevoCorreo).isPresent()) {
            throw new IllegalArgumentException(
                    "El nuevo correo ya está registrado"
            );
        }

        codigoVerificacionService.enviarCodigoCambioCorreo(
                nuevoCorreo
        );
    }

    public UsuarioDTO verificarCambioCorreo(
            String emailActual,
            String nuevoCorreo,
            String codigo) {

        Usuario usuario = usuarioRepository
                .findByEmail(emailActual)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Usuario no encontrado"
                        )
                );

        if (usuarioRepository.findByEmail(nuevoCorreo).isPresent()) {
            throw new IllegalArgumentException(
                    "El nuevo correo ya está registrado"
            );
        }

        codigoVerificacionService.validarCodigoCambioCorreo(
                nuevoCorreo,
                codigo
        );

        usuario.setEmail(nuevoCorreo);
        usuario.setCorreoVerificado(true);

        usuarioRepository.save(usuario);

        return convertirDTO(usuario);
    }
}