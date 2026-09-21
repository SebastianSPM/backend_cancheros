package com.generation.grupo10.repository;

import com.generation.grupo10.model.Cancha;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface CanchaRepository extends JpaRepository<Cancha, Long> {

    @Query("""
            SELECT c
            FROM Cancha c
            WHERE LOWER(c.ubicacion) LIKE LOWER(CONCAT('%', COALESCE(:ubicacion, ''), '%'))
            AND LOWER(c.tipo) LIKE LOWER(CONCAT('%', COALESCE(:tipo, ''), '%'))
            AND (:precioMin IS NULL OR c.precioPorHora >= :precioMin)
            AND (:precioMax IS NULL OR c.precioPorHora <= :precioMax)
            """)
    Page<Cancha> buscarConFiltros(
            @Param("ubicacion") String ubicacion,
            @Param("tipo") String tipo,
            @Param("precioMin") BigDecimal precioMin,
            @Param("precioMax") BigDecimal precioMax,
            Pageable pageable
    );
}