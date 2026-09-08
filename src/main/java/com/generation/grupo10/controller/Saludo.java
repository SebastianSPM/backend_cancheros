package com.generation.grupo10.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Saludo {
    @GetMapping("/saludo")
    public String saludar() {
        return "¡Hola, Mundo!";
    }
}
