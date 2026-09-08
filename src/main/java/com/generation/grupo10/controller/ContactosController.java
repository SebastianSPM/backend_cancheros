package com.generation.grupo10.controller;
import com.generation.grupo10.model.Contactos;
import com.generation.grupo10.repository.ContactosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/contactos")
@CrossOrigin(origins = "*")
public class ContactosController {
    @Autowired
    private ContactosRepository contactosRepository;
    @GetMapping
    public List<Contactos> obtenerTodos() {
        return contactosRepository.findAll();
    }
    @PostMapping
    public ResponseEntity<Contactos> guardarContacto(@RequestBody Contactos contacto) {
        Contactos nuevoContacto = contactosRepository.save(contacto);
        return new ResponseEntity<>(nuevoContacto, HttpStatus.CREATED);
    }
}