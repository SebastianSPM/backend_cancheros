package com.generation.grupo10.service;

import com.generation.grupo10.model.CodigoVerificacion;
import com.generation.grupo10.model.Usuario;
import com.generation.grupo10.repository.CodigoVerificacionRepository;
import com.generation.grupo10.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CodigoVerificacionService {

    private final CodigoVerificacionRepository codigoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService;

    public void enviarCodigo(String email) {

        String codigo = generarCodigo();

        CodigoVerificacion codigoVerificacion =
                new CodigoVerificacion();

        codigoVerificacion.setEmail(email);
        codigoVerificacion.setCodigo(codigo);

        codigoVerificacion.setFechaExpiracion(
                LocalDateTime.now().plusMinutes(10)
        );

        codigoVerificacion.setUsado(false);

        codigoRepository.save(codigoVerificacion);

        emailService.enviarCodigo(
                email,
                codigo
        );
    }

    public void verificarCodigo(String email, String codigo) {

        CodigoVerificacion codigoVerificacion =
                codigoRepository
                        .findTopByEmailAndUsadoFalseOrderByIdDesc(email)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Código no encontrado o ya utilizado"
                                )
                        );

        if (codigoVerificacion
                .getFechaExpiracion()
                .isBefore(LocalDateTime.now())) {

            throw new IllegalArgumentException(
                    "El código ha expirado"
            );
        }

        if (!codigoVerificacion
                .getCodigo()
                .equals(codigo)) {

            throw new IllegalArgumentException(
                    "Código incorrecto"
            );
        }

        codigoVerificacion.setUsado(true);
        codigoRepository.save(codigoVerificacion);

        Usuario usuario =
                usuarioRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Usuario no encontrado"
                                )
                        );

        usuario.setCorreoVerificado(true);
        usuarioRepository.save(usuario);
    }

    private String generarCodigo() {

        int numero =
                100000 + new Random().nextInt(900000);

        return String.valueOf(numero);
    }
}