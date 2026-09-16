package com.generation.grupo10.repository;

import com.generation.grupo10.enums.TokenPurpose;
import com.generation.grupo10.model.TokenRecuperacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRecuperacionRepository
        extends JpaRepository<TokenRecuperacion, Long> {

    Optional<TokenRecuperacion> findByTokenAndUsadoFalse(
            String token
    );

    Optional<TokenRecuperacion> findByTokenAndPurposeAndUsadoFalse(
            String token,
            TokenPurpose purpose
    );

}