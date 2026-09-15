package com.generation.grupo10.repository;

import com.generation.grupo10.model.CodigoVerificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodigoVerificacionRepository
        extends JpaRepository<CodigoVerificacion, Long> {

    Optional<CodigoVerificacion> findTopByEmailAndUsadoFalseOrderByIdDesc(
            String email
    );
}