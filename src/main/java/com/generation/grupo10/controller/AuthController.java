package com.generation.grupo10.controller;

import com.generation.grupo10.dto.LoginRequest;
import com.generation.grupo10.dto.LoginResponse;
import com.generation.grupo10.model.Usuario;
import com.generation.grupo10.repository.UsuarioRepository;
import com.generation.grupo10.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

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

import java.time.Duration;

import org.springframework.security.core.Authentication;

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

    @Operation(summary = "Iniciar sesión", description = "Autentica un usuario y genera un token JWT.")
    @ApiResponses(
            {
                @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso"),
                @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
            }
    )
    //Login

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail()).orElse(null);

        if (usuario == null) {
            return ResponseEntity.badRequest().body("Email o contraseña incorrectos");
        }

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {

            return ResponseEntity
                    .badRequest()
                    .body("Email o contraseña incorrectos");
        }

        if (!usuario.isCorreoVerificado()) {
            return ResponseEntity.badRequest().body("Debes verificar tu correo antes de iniciar sesión");
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

        String token = jwtService.generarToken(usuario.getEmail(), usuario.getRol());

        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .secure(false) // PENDIENTE {OJO} ESTO SE DEBE CAMBIAR A TRUE EN PRODUCCIÓN
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofHours(2))
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(usuarioDTO);
    }

    // swagger
    @Operation(summary = "Registrar usuario", description = "Registra un nuevo usuario en el sistema.")
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "200", description = "Usuario registrado correctamente"),
                    @ApiResponse(responseCode = "400", description = "El correo ya está registrado o los datos son inválidos")
            }
    )
    //Register
    @PostMapping("/registro")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("El email ya está registrado");
        }

        Usuario usuario = new Usuario();

        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setEmail(request.getEmail());
        usuario.setTelefono(request.getTelefono());

        usuario.setPassword(passwordEncoder.encode(request.getPassword()));

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

    @Operation(summary = "Verificar correo electrónico", description = "Verifica el código enviado al correo electrónico del usuario."
    )
    @ApiResponses(
            {
                @ApiResponse(responseCode = "200", description = "Correo verificado correctamente"),
                @ApiResponse(responseCode = "400", description = "Código inválido o expirado")
            }
    )

    //verificar el correo

    @PostMapping("/verificar-correo")
    public ResponseEntity<?> verificarCorreo(@RequestBody VerificarCodigoRequest request) {

        try {
            codigoVerificacionService.verificarCodigo(request.getEmail(), request.getCodigo());
            return ResponseEntity.ok("Correo verificado correctamente");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Solicitar recuperación de contraseña",
            description = "Envía un enlace de recuperación de contraseña al correo indicado."
    )
    @ApiResponses(
            {
                @ApiResponse(responseCode = "200", description = "Solicitud procesada correctamente"),
                @ApiResponse(responseCode = "400", description = "Correo inválido o solicitud incorrecta")
            }
    )

    //olvidar clave

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {

        try {
            recuperacionPasswordService.solicitarRecuperacion(request.getEmail());

            return ResponseEntity.ok("Enlace de recuperación enviado");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Restablecer contraseña", description = "Cambia la contraseña utilizando un token de recuperación válido.")
    @ApiResponses(
            {

                    @ApiResponse(responseCode = "200", description = "Contraseña restablecida correctamente"),
                    @ApiResponse(responseCode = "400", description = "Token inválido, expirado o datos incorrectos")
            }
    )

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {

        try {

            recuperacionPasswordService.cambiarPassword(request.getToken(), request.getNuevaPassword());
            return ResponseEntity.ok("Contraseña actualizada correctamente");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {

        ResponseCookie cookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .secure(false) // true en producción con HTTPS
                .sameSite("Lax")
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Sesión cerrada correctamente");
    }

    @PostMapping("/solicitar-cambio-password")
    public ResponseEntity<?> solicitarCambioPassword(
            Authentication authentication) {

        try {

            String email = authentication.getName();

            recuperacionPasswordService.solicitarCambioPassword(email);

            return ResponseEntity.ok(
                    "Se ha enviado un enlace de validación a tu correo"
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @GetMapping("/validar-cambio-password")
    public ResponseEntity<?> validarCambioPassword(
            @RequestParam String token) {

        try {

            recuperacionPasswordService.validarCambioPassword(token);

            return ResponseEntity.ok(
                    "Correo validado correctamente"
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}