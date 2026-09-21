package com.generation.grupo10.service;
import com.generation.grupo10.model.CanchaServicio;
import com.generation.grupo10.model.CanchaServicioId;
import com.generation.grupo10.repository.CanchaServicioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CanchaServicioService {

    private final CanchaServicioRepository canchaServicioRepository;

    public CanchaServicioService(CanchaServicioRepository canchaServicioRepository) {
        this.canchaServicioRepository = canchaServicioRepository;
    }

    public CanchaServicio guardar(CanchaServicio canchaServicio) {
        return canchaServicioRepository.save(canchaServicio);
    }

    public List<CanchaServicio> listarTodos() {
        return canchaServicioRepository.findAll();
    }

    public void eliminar(CanchaServicioId id) {
        canchaServicioRepository.deleteById(id);
    }
}