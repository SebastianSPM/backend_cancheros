package com.generation.grupo10.service;

import com.generation.grupo10.model.Usuario;
import com.generation.grupo10.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.Random;
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario guardarUsuario(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    // =====================================================
    // GENERAR CÓDIGO DE RECUPERACIÓN
    // =====================================================

    public String generarCodigoRecuperacion(String email) {

        Usuario usuario = usuarioRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No existe un usuario con ese correo"
                        )
                );

        // Generar código de 6 dígitos
        String codigo = String.format(
                "%06d",
                new Random().nextInt(1_000_000)
        );

        // Guardar código
        usuario.setCodigoRecuperacion(codigo);

        // El código expira en 10 minutos
        usuario.setCodigoRecuperacionExpiracion(
                LocalDateTime.now().plusMinutes(10)
        );

        usuarioRepository.save(usuario);

        return codigo;
    }

    // =====================================================
    // VERIFICAR CÓDIGO DE RECUPERACIÓN
    // =====================================================

    public void verificarCodigo(
            String email,
            String codigo) {

        Usuario usuario = usuarioRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Usuario no encontrado"
                        )
                );

        // Verificar que exista código
        if (usuario.getCodigoRecuperacion() == null) {

            throw new IllegalArgumentException(
                    "No existe un código de recuperación"
            );
        }

        // Verificar código
        if (!usuario
                .getCodigoRecuperacion()
                .equals(codigo)) {

            throw new IllegalArgumentException(
                    "Código de recuperación incorrecto"
            );
        }

        // Verificar expiración
        if (usuario.getCodigoRecuperacionExpiracion() == null
                || usuario
                .getCodigoRecuperacionExpiracion()
                .isBefore(LocalDateTime.now())) {

            throw new IllegalArgumentException(
                    "El código de recuperación ha expirado"
            );
        }
    }

    // =====================================================
    // RESTABLECER CONTRASEÑA
    // =====================================================

    public void restablecerPassword(
            String email,
            String codigo,
            String nuevaPassword) {

        // ---------------------------------------------
        // VALIDAR NUEVA CONTRASEÑA
        // ---------------------------------------------

        if (nuevaPassword == null
                || nuevaPassword.isBlank()) {

            throw new IllegalArgumentException(
                    "La nueva contraseña es obligatoria"
            );
        }

        if (nuevaPassword.length() < 6) {

            throw new IllegalArgumentException(
                    "La contraseña debe tener al menos 6 caracteres"
            );
        }

        // ---------------------------------------------
        // BUSCAR USUARIO
        // ---------------------------------------------

        Usuario usuario = usuarioRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Usuario no encontrado"
                        )
                );

        // ---------------------------------------------
        // VALIDAR CÓDIGO
        // ---------------------------------------------

        verificarCodigo(
                email,
                codigo
        );

        // ---------------------------------------------
        // ENCRIPTAR NUEVA CONTRASEÑA
        // ---------------------------------------------

        usuario.setPassword(
                passwordEncoder.encode(
                        nuevaPassword
                )
        );

        // ---------------------------------------------
        // ELIMINAR CÓDIGO UTILIZADO
        // ---------------------------------------------

        usuario.setCodigoRecuperacion(null);

        usuario.setCodigoRecuperacionExpiracion(null);

        // ---------------------------------------------
        // GUARDAR CAMBIOS
        // ---------------------------------------------

        usuarioRepository.save(usuario);
    }

}