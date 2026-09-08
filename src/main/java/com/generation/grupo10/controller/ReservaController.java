package com.generation.grupo10.controller;



import com.generation.grupo10.dto.ReservaRequest;
import com.generation.grupo10.dto.ReservaResponse;
import com.generation.grupo10.service.ReservaService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
public class ReservaController {


    private final ReservaService reservaService;


    @PostMapping
    public ResponseEntity<ReservaResponse> crearReserva(
            @RequestBody ReservaRequest request) {

        ReservaResponse response =
                reservaService.crearReserva(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}