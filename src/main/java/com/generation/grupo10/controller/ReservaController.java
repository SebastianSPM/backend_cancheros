package com.generation.grupo10.controller;

import com.generation.grupo10.dto.DisponibilidadResponse;
import com.generation.grupo10.dto.ReservaRequest;
import com.generation.grupo10.dto.ReservaResponse;
import com.generation.grupo10.dto.ReservaResumen;
import com.generation.grupo10.service.ReservaService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReservaController {

    private final ReservaService reservaService;


    // =========================================================
    // CREAR RESERVA
    // =========================================================

    @PostMapping
    public ResponseEntity<ReservaResponse> crearReserva(
            @RequestParam Long usuarioId,
            @RequestBody ReservaRequest request) {

        ReservaResponse response =
                reservaService.crearReserva(
                        usuarioId,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // =========================================================
    // MIS RESERVAS
    // =========================================================

    @GetMapping("/mis-reservas")
    public ResponseEntity<List<ReservaResumen>> misReservas(
            @RequestParam Long usuarioId) {

        return ResponseEntity.ok(
                reservaService.obtenerMisReservas(
                        usuarioId
                )
        );
    }


    // =========================================================
    // DETALLE
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponse> obtenerPorId(
            @PathVariable Long id,
            @RequestParam Long usuarioId) {

        return ResponseEntity.ok(
                reservaService.obtenerPorId(
                        id,
                        usuarioId
                )
        );
    }


    // =========================================================
    // MODIFICAR
    // =========================================================

    @PutMapping("/{id}")
    public ResponseEntity<ReservaResponse> actualizar(
            @PathVariable Long id,
            @RequestParam Long usuarioId,
            @RequestBody ReservaRequest request) {

        return ResponseEntity.ok(
                reservaService.actualizarReserva(
                        id,
                        usuarioId,
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
            @RequestParam Long usuarioId) {

        reservaService.cancelarReserva(
                id,
                usuarioId
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