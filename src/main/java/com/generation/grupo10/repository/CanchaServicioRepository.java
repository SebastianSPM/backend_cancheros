package com.generation.grupo10.repository;

import com.generation.grupo10.model.CanchaServicio;
import com.generation.grupo10.model.CanchaServicioId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CanchaServicioRepository
        extends JpaRepository<CanchaServicio, CanchaServicioId> {
}
