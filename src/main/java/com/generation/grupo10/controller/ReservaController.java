package com.generation.grupo10.controller;

import com.generation.grupo10.dto.DisponibilidadResponse;
import com.generation.grupo10.dto.ReservaRequest;
import com.generation.grupo10.dto.ReservaResponse;
import com.generation.grupo10.dto.ReservaResumen;
import com.generation.grupo10.service.ReservaService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;


    // =========================================================
    // CREAR RESERVA
    // =========================================================

    @PostMapping
    public ResponseEntity<ReservaResponse> crearReserva(
            Authentication authentication,
            @RequestBody ReservaRequest request) {

        String email = authentication.getName();

        ReservaResponse response =
                reservaService.crearReserva(email, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =========================================================
    // MIS RESERVAS
    // =========================================================

    @GetMapping("/mis-reservas")
    public ResponseEntity<List<ReservaResumen>> misReservas(
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                reservaService.obtenerMisReservas(email)
        );
    }


    // =========================================================
    // DETALLE
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponse> obtenerPorId(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                reservaService.obtenerPorId(
                        id,
                        email
                )
        );
    }


    // =========================================================
    // MODIFICAR
    // =========================================================

    @PutMapping("/{id}")
    public ResponseEntity<ReservaResponse> actualizar(
            @PathVariable Long id,
            Authentication authentication,
            @RequestBody ReservaRequest request) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                reservaService.actualizarReserva(
                        id,
                        email,
                        request
                )
        );
    }


    // =========================================================
    // CANCELAR
    // =========================================================

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();

        reservaService.cancelarReserva(
                id,
                email
        );

        return ResponseEntity.noContent().build();
    }


    // =========================================================
    // DISPONIBILIDAD
    // =========================================================

    @GetMapping("/disponibilidad")
    public ResponseEntity<List<DisponibilidadResponse>>
    disponibilidad(
            @RequestParam Long canchaId,
            @RequestParam LocalDate fecha) {

        return ResponseEntity.ok(
                reservaService.consultarDisponibilidad(
                        canchaId,
                        fecha
                )
        );
    }
}