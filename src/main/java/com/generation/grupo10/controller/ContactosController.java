package com.generation.grupo10.controller;

import com.generation.grupo10.dto.ContactoRequest;
import com.generation.grupo10.dto.ContactoResponse;
import com.generation.grupo10.service.ContactosService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contactos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ContactosController {

    private final ContactosService contactosService;

    @GetMapping
    public List<ContactoResponse> obtenerTodos() {
        return contactosService.obtenerTodos();
    }

    @PostMapping
    public ResponseEntity<ContactoResponse> guardarContacto(
            @RequestBody ContactoRequest request) {

        ContactoResponse nuevoContacto =
                contactosService.guardarContacto(request);

        return new ResponseEntity<>(nuevoContacto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarContacto(@PathVariable Integer id) {

        contactosService.eliminarContacto(id);

        return ResponseEntity.noContent().build();
    }
}