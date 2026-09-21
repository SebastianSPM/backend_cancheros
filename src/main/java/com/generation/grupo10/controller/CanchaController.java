package com.generation.grupo10.controller;

import com.generation.grupo10.dto.CanchaDTO;
import com.generation.grupo10.service.CanchaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//Paginación en canchas
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/canchas")
@RequiredArgsConstructor
public class CanchaController {

    private final CanchaService canchaService;

    @GetMapping
    public ResponseEntity<Page<CanchaDTO>> obtenerTodas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(required = false) String ubicacion,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) BigDecimal precioMin,
            @RequestParam(required = false) BigDecimal precioMax) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                canchaService.obtenerTodas(
                        ubicacion,
                        tipo,
                        precioMin,
                        precioMax,
                        pageable
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CanchaDTO> obtenerPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                canchaService.obtenerPorId(id)
        );
    }

    @PostMapping("/{canchaId}/servicios/{servicioId}")
    public ResponseEntity<Void> asignarServicio(
            @PathVariable Long canchaId,
            @PathVariable Long servicioId) {

        canchaService.asignarServicio(canchaId, servicioId);

        return ResponseEntity.ok().build();
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

    @PostMapping(
            value = "/{id}/imagenes",
            consumes = "multipart/form-data"
    )
    public ResponseEntity<CanchaDTO> subirImagenes(
            @PathVariable Long id,
            @RequestParam("imagenes")
            List<MultipartFile> imagenes) {

        return ResponseEntity.ok(
                canchaService.subirImagenes(
                        id,
                        imagenes
                )
        );
    }
}