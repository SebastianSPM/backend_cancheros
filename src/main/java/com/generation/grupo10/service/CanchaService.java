package com.generation.grupo10.service;


import com.generation.grupo10.dto.CanchaDTO;
import com.generation.grupo10.model.Cancha;
import com.generation.grupo10.repository.CanchaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CanchaService {

    private final CanchaRepository canchaRepository;

    public List<CanchaDTO> obtenerTodas() {

        return canchaRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    public CanchaDTO obtenerPorId(Long id) {

        Cancha cancha = canchaRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Cancha no encontrada con id: " + id)
                );

        return convertirADTO(cancha);
    }

    public CanchaDTO guardar(CanchaDTO dto) {

        Cancha cancha = new Cancha();

        cancha.setNombreCancha(dto.getNombreCancha());
        cancha.setUbicacion(dto.getUbicacion());
        cancha.setDescripcion(dto.getDescripcion());
        cancha.setPrecioPorHora(dto.getPrecioPorHora());
        cancha.setTipo(dto.getTipo());
        cancha.setRating(dto.getRating());
        cancha.setTotalResenas(dto.getTotalResenas());
        cancha.setImagenUrl(dto.getImagenUrl());
        cancha.setDisponible(dto.getDisponible());

        Cancha canchaGuardada = canchaRepository.save(cancha);

        return convertirADTO(canchaGuardada);
    }

    public CanchaDTO actualizar(Long id, CanchaDTO dto) {

        Cancha cancha = canchaRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Cancha no encontrada con id: " + id)
                );

        cancha.setNombreCancha(dto.getNombreCancha());
        cancha.setUbicacion(dto.getUbicacion());
        cancha.setDescripcion(dto.getDescripcion());
        cancha.setPrecioPorHora(dto.getPrecioPorHora());
        cancha.setTipo(dto.getTipo());
        cancha.setRating(dto.getRating());
        cancha.setTotalResenas(dto.getTotalResenas());
        cancha.setImagenUrl(dto.getImagenUrl());
        cancha.setDisponible(dto.getDisponible());

        Cancha canchaActualizada = canchaRepository.save(cancha);

        return convertirADTO(canchaActualizada);
    }

    public void eliminar(Long id) {

        if (!canchaRepository.existsById(id)) {
            throw new RuntimeException(
                    "Cancha no encontrada con id: " + id
            );
        }

        canchaRepository.deleteById(id);
    }

    private CanchaDTO convertirADTO(Cancha cancha) {

        return new CanchaDTO(
                cancha.getId(),
                cancha.getNombreCancha(),
                cancha.getUbicacion(),
                cancha.getDescripcion(),
                cancha.getPrecioPorHora(),
                cancha.getTipo(),
                cancha.getRating(),
                cancha.getTotalResenas(),
                cancha.getImagenUrl(),
                cancha.getDisponible()
        );
    }
}