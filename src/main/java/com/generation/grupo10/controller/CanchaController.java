package com.generation.grupo10.controller;

import com.generation.grupo10.dto.CanchaDTO;
import com.generation.grupo10.service.CanchaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/canchas")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CanchaController {

    private final CanchaService canchaService;

    @GetMapping
    public ResponseEntity<List<CanchaDTO>> obtenerTodas() {

        return ResponseEntity.ok(
                canchaService.obtenerTodas()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CanchaDTO> obtenerPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                canchaService.obtenerPorId(id)
        );
    }

    @PostMapping
    public ResponseEntity<CanchaDTO> guardar(
            @RequestBody CanchaDTO dto) {

        CanchaDTO canchaGuardada = canchaService.guardar(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(canchaGuardada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CanchaDTO> actualizar(
            @PathVariable Long id,
            @RequestBody CanchaDTO dto) {

        return ResponseEntity.ok(
                canchaService.actualizar(id, dto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        canchaService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}