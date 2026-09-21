package com.generation.grupo10.service;


import com.generation.grupo10.dto.CanchaDTO;
import com.generation.grupo10.model.Cancha;
import com.generation.grupo10.model.CanchaServicio;
import com.generation.grupo10.model.Servicio;
import com.generation.grupo10.repository.CanchaRepository;
import com.generation.grupo10.repository.CanchaServicioRepository;
import com.generation.grupo10.repository.ServicioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

//Paginación en canchas
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.generation.grupo10.model.CanchaImagen;
//array
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CanchaService {

    private final CanchaRepository canchaRepository;
    private final ServicioRepository servicioRepository;
    private final CanchaServicioRepository canchaServicioRepository;
    private final CloudinaryService cloudinaryService;

    @Transactional(readOnly = true)
    public Page<CanchaDTO> obtenerTodas(
            String ubicacion,
            String tipo,
            BigDecimal precioMin,
            BigDecimal precioMax,
            Pageable pageable) {

        return canchaRepository.buscarConFiltros(
                ubicacion,
                tipo,
                precioMin,
                precioMax,
                pageable
        ).map(this::convertirADTO);
    }

    @Transactional(readOnly = true)
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

        List<String> imagenes =
                obtenerUrlsImagenes(cancha);

        String primeraImagen =
                imagenes.isEmpty()
                        ? null
                        : imagenes.get(0);

        return new CanchaDTO(
                cancha.getId(),
                cancha.getNombreCancha(),
                cancha.getUbicacion(),
                cancha.getDescripcion(),
                cancha.getPrecioPorHora(),
                cancha.getTipo(),
                cancha.getRating(),
                cancha.getTotalResenas(),
                primeraImagen,
                imagenes,
                cancha.getDisponible()
        );
    }

    public void asignarServicio(Long canchaId, Long servicioId) {

        Cancha cancha = canchaRepository.findById(canchaId)
                .orElseThrow(() ->
                        new RuntimeException("Cancha no encontrada")
                );

        Servicio servicio = servicioRepository.findById(servicioId)
                .orElseThrow(() ->
                        new RuntimeException("Servicio no encontrado")
                );

        CanchaServicio canchaServicio =
                new CanchaServicio(cancha, servicio);

        canchaServicioRepository.save(canchaServicio);
    }

    @Transactional
    public CanchaDTO subirImagenes(
            Long id,
            List<MultipartFile> archivos) {

        Cancha cancha = canchaRepository.findById(id).orElseThrow(() -> new RuntimeException("Cancha no encontrada con id: " + id));

        if (archivos == null || archivos.isEmpty()) {
            throw new IllegalArgumentException("Debes seleccionar al menos una imagen.");
        }

        int imagenesActuales = obtenerUrlsImagenes(cancha).size();

        if (imagenesActuales + archivos.size() > 3) {
            throw new IllegalArgumentException(
                    "Una cancha puede tener máximo 3 imágenes."
            );
        }

        int ordenActual =
                imagenesActuales;

        for (MultipartFile archivo : archivos) {

            CloudinaryService.ResultadoImagen resultado =
                    cloudinaryService.subirImagen(
                            archivo,
                            "cancheros/canchas/" + id
                    );

            CanchaImagen imagen =
                    new CanchaImagen();

            imagen.setUrl(
                    resultado.url()
            );

            imagen.setPublicId(
                    resultado.publicId()
            );

            imagen.setOrden(
                    ordenActual++
            );

            imagen.setCancha(
                    cancha
            );

            cancha.getImagenes().add(
                    imagen
            );
        }

        canchaRepository.save(cancha);

        return convertirADTO(cancha);
    }

    private List<String> obtenerUrlsImagenes(Cancha cancha) {

        List<String> urls = new ArrayList<>();

        // Imagen antigua, por compatibilidad
        if (
                cancha.getImagenUrl() != null &&
                        !cancha.getImagenUrl().isBlank()
        ) {
            urls.add(cancha.getImagenUrl());
        }

        // Nuevas imágenes de Cloudinary
        if (cancha.getImagenes() != null) {

            urls.addAll(
                    cancha.getImagenes()
                            .stream()
                            .map(CanchaImagen::getUrl)
                            .toList()
            );
        }

        return urls;
    }
}