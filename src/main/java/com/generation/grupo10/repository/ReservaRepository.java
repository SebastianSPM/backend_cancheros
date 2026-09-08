package com.generation.grupo10.repository;



import com.generation.grupo10.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByFecha(LocalDate fecha);

    List<Reserva> findByCanchaIdAndFecha(
            Long canchaId,
            LocalDate fecha
    );
}