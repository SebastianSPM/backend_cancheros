package com.generation.grupo10.controller;

import com.generation.grupo10.dto.LoginRequest;
import com.generation.grupo10.dto.LoginResponse;
import com.generation.grupo10.model.Usuario;
import com.generation.grupo10.repository.UsuarioRepository;
import com.generation.grupo10.service.JwtService;
import org.springframework.http.ResponseEntity;
import com.generation.grupo10.dto.CambiarPasswordRequest;
import org.springframework.security.core.Authentication;

//encriptar clave
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.bind.annotation.*;
import com.generation.grupo10.dto.UsuarioDTO;
import com.generation.grupo10.dto.RegisterRequest;

import com.generation.grupo10.service.CodigoVerificacionService;

//Verificar codigo para correo
import com.generation.grupo10.dto.VerificarCodigoRequest;

//Olvidar clave
import com.generation.grupo10.dto.ForgotPasswordRequest;
import com.generation.grupo10.service.RecuperacionPasswordService;
import com.generation.grupo10.dto.ResetPasswordRequest;

//swagger
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "Endpoints para la autenticación de usuarios")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final CodigoVerificacionService codigoVerificacionService;

    private final RecuperacionPasswordService recuperacionPasswordService;

    public AuthController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService, CodigoVerificacionService codigoVerificacionService, RecuperacionPasswordService recuperacionPasswordService) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.codigoVerificacionService = codigoVerificacionService;
        this.recuperacionPasswordService = recuperacionPasswordService;
    }

    //Login

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        Usuario usuario = usuarioRepository
                .findByEmail(request.getEmail())
                .orElse(null);

        if (usuario == null) {
            return ResponseEntity
                    .badRequest()
                    .body("Email o contraseña incorrectos");
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                usuario.getPassword())) {

            return ResponseEntity
                    .badRequest()
                    .body("Email o contraseña incorrectos");
        }

        if (!usuario.isCorreoVerificado()) {

            return ResponseEntity
                    .badRequest()
                    .body("Debes verificar tu correo antes de iniciar sesión");
        }

        UsuarioDTO usuarioDTO = new UsuarioDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getTelefono(),
                usuario.getFotoPerfil(),
                usuario.getRol(),
                usuario.getFechaCreacion()
        );

        String token = jwtService.generarToken(
                usuario.getEmail(),
                usuario.getRol()
        );

        return ResponseEntity.ok(
                new LoginResponse(token, usuarioDTO)
        );
    }

    //Register

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body("El email ya está registrado");
        }

        Usuario usuario = new Usuario();

        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setEmail(request.getEmail());
        usuario.setTelefono(request.getTelefono());

        usuario.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        usuario.setRol("CLIENTE");

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        codigoVerificacionService.enviarCodigo(
                usuarioGuardado.getEmail()
        );

        UsuarioDTO usuarioDTO = new UsuarioDTO(
                usuarioGuardado.getId(),
                usuarioGuardado.getNombre(),
                usuarioGuardado.getApellido(),
                usuarioGuardado.getEmail(),
                usuarioGuardado.getTelefono(),
                usuarioGuardado.getFotoPerfil(),
                usuarioGuardado.getRol(),
                usuarioGuardado.getFechaCreacion()
        );

        return ResponseEntity.ok(usuarioDTO);
    }


    @PostMapping("/test-code")
    public ResponseEntity<String> enviarCodigoPrueba(
            @RequestParam String email) {

        codigoVerificacionService.enviarCodigo(email);

        return ResponseEntity.ok(
                "Código enviado correctamente"
        );
    }

    @PostMapping("/verificar-correo")
    public ResponseEntity<?> verificarCorreo(
            @RequestBody VerificarCodigoRequest request) {

        try {

            codigoVerificacionService.verificarCodigo(
                    request.getEmail(),
                    request.getCodigo()
            );

            return ResponseEntity.ok(
                    "Correo verificado correctamente"
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @RequestBody ForgotPasswordRequest request) {

        try {

            recuperacionPasswordService.solicitarRecuperacion(
                    request.getEmail()
            );

            return ResponseEntity.ok(
                    "Enlace de recuperación enviado"
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestBody ResetPasswordRequest request) {

        try {

            recuperacionPasswordService.cambiarPassword(
                    request.getToken(),
                    request.getNuevaPassword()
            );

            return ResponseEntity.ok(
                    "Contraseña actualizada correctamente"
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
    //Cambiar contraseña desde el perfil

    @PostMapping("/cambiar-password")
    public ResponseEntity<?> cambiarPassword(
            @RequestBody CambiarPasswordRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        Usuario usuario = usuarioRepository
                .findByEmail(email)
                .orElse(null);

        if (usuario == null) {
            return ResponseEntity
                    .badRequest()
                    .body("Usuario no encontrado");
        }

        // Verificar que la contraseña actual sea correcta

        if (!passwordEncoder.matches(
                request.getPasswordActual(),
                usuario.getPassword())) {

            return ResponseEntity
                    .badRequest()
                    .body("La contraseña actual es incorrecta");
        }

        // Encriptar y guardar la nueva contraseña

        usuario.setPassword(
                passwordEncoder.encode(request.getNuevaPassword())
        );

        usuarioRepository.save(usuario);

        return ResponseEntity.ok(
                "Contraseña actualizada correctamente"
        );
    }
}