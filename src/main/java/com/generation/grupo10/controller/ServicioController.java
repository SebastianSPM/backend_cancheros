package com.cancheros.controller;

import com.cancheros.dto.ServicioDTO;
import com.cancheros.model.Servicio;
import com.cancheros.service.ServicioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicios")
@CrossOrigin(origins = "*")
public class ServicioController {

    private final ServicioService servicioService;

    public ServicioController(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    // GET: Obtener lista de todos los servicios para los checkboxes del formulario
    @GetMapping
    public ResponseEntity<List<Servicio>> listar() {
        return ResponseEntity.ok(servicioService.listarTodos());
    }

    // POST: Crear un nuevo servicio
    @PostMapping
    public ResponseEntity<Servicio> crear(@RequestBody ServicioDTO dto) {
        return new ResponseEntity<>(servicioService.crearServicio(dto), HttpStatus.CREATED);
    }

    // POST: Asociar un servicio existente a una cancha específica
    @PostMapping("/{servicioId}/asociar/{canchaId}")
    public ResponseEntity<Void> asociarACancha(@PathVariable Long servicioId, @PathVariable Long canchaId) {
        servicioService.asignarServicioACancha(canchaId, servicioId);
        return ResponseEntity.ok().build();
    }

    // DELETE: Eliminar un servicio
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicioService.eliminarServicio(id);
        return ResponseEntity.noContent().build();
    }
}