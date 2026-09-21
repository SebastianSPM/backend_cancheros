package com.generation.grupo10.service;

import com.generation.grupo10.model.TokenRecuperacion;
import com.generation.grupo10.repository.TokenRecuperacionRepository;
import com.generation.grupo10.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.generation.grupo10.enums.TokenPurpose;

@Service
@RequiredArgsConstructor
public class RecuperacionPasswordService {

    private final UsuarioRepository usuarioRepository;
    private final TokenRecuperacionRepository tokenRepository;
    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;

    public void solicitarRecuperacion(String email) {

        usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No existe un usuario con ese email"
                        )
                );

        String token = UUID.randomUUID().toString();

        TokenRecuperacion tokenRecuperacion =
                new TokenRecuperacion();

        tokenRecuperacion.setEmail(email);
        tokenRecuperacion.setToken(token);

        tokenRecuperacion.setFechaExpiracion(
                LocalDateTime.now().plusMinutes(15)
        );

        tokenRecuperacion.setUsado(false);
        tokenRecuperacion.setPurpose(TokenPurpose.PASSWORD_RESET);

        tokenRepository.save(tokenRecuperacion);

        String enlace =
                "http://localhost:5502/pages/auth/nueva-password.html?token="
                        + token;

        emailService.enviarEnlaceRecuperacion(
                email,
                enlace
        );
    }

    public void cambiarPassword(
            String token,
            String nuevaPassword) {

        TokenRecuperacion tokenRecuperacion =
                tokenRepository
                        .findByTokenAndUsadoFalseAndPurposeIn(
                                token,
                                List.of(
                                        TokenPurpose.PASSWORD_RESET,
                                        TokenPurpose.PASSWORD_CHANGE
                                )
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Token inválido o ya utilizado"
                                )
                        );

        if (tokenRecuperacion
                .getFechaExpiracion()
                .isBefore(LocalDateTime.now())) {

            throw new IllegalArgumentException(
                    "El enlace ha expirado"
            );
        }

        var usuario =
                usuarioRepository
                        .findByEmail(
                                tokenRecuperacion.getEmail()
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Usuario no encontrado"
                                )
                        );

        usuario.setPassword(
                passwordEncoder.encode(nuevaPassword)
        );

        usuarioRepository.save(usuario);

        tokenRecuperacion.setUsado(true);

        tokenRepository.save(tokenRecuperacion);
    }

    public void solicitarCambioPassword(String email) {

        usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Usuario no encontrado"
                        )
                );

        String token = UUID.randomUUID().toString();

        TokenRecuperacion tokenRecuperacion =
                new TokenRecuperacion();

        tokenRecuperacion.setEmail(email);
        tokenRecuperacion.setToken(token);

        tokenRecuperacion.setFechaExpiracion(
                LocalDateTime.now().plusMinutes(15)
        );

        tokenRecuperacion.setUsado(false);

        tokenRecuperacion.setPurpose(
                TokenPurpose.PASSWORD_CHANGE
        );

        tokenRepository.save(tokenRecuperacion);

        String enlace =
                "http://localhost:5502/pages/auth/validar-cambio-password.html?token="
                        + token;

        emailService.enviarEnlaceCambioPassword(
                email,
                enlace
        );
    }

    public void validarCambioPassword(String token) {
        TokenRecuperacion tokenRecuperacion =
                tokenRepository
                        .findByTokenAndPurposeAndUsadoFalse(
                                token,
                                TokenPurpose.PASSWORD_CHANGE
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Token inválido o ya utilizado"
                                )
                        );
        if (tokenRecuperacion
                .getFechaExpiracion()
                .isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "El enlace ha expirado"
            );
        }
    }

    public void solicitarEdicionPerfil(String email) {

        usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Usuario no encontrado"
                        )
                );

        String token = UUID.randomUUID().toString();

        TokenRecuperacion tokenRecuperacion =
                new TokenRecuperacion();

        tokenRecuperacion.setEmail(email);
        tokenRecuperacion.setToken(token);

        tokenRecuperacion.setFechaExpiracion(
                LocalDateTime.now().plusMinutes(15)
        );

        tokenRecuperacion.setUsado(false);

        tokenRecuperacion.setPurpose(
                TokenPurpose.PROFILE_EDIT
        );

        tokenRepository.save(tokenRecuperacion);

        String enlace =
                "http://localhost:5502/pages/usuario/validar-edicion-perfil.html?token="
                        + token;

        emailService.enviarEnlaceEdicionPerfil(
                email,
                enlace
        );
    }

    public void validarEdicionPerfil(String token) {

        TokenRecuperacion tokenRecuperacion =
                tokenRepository
                        .findByTokenAndPurposeAndUsadoFalse(
                                token,
                                TokenPurpose.PROFILE_EDIT
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Token inválido o ya utilizado"
                                )
                        );

        if (tokenRecuperacion
                .getFechaExpiracion()
                .isBefore(LocalDateTime.now())) {

            throw new IllegalArgumentException(
                    "El enlace ha expirado"
            );
        }
    }
}