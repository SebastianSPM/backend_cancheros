package com.generation.grupo10.repository;

import com.generation.grupo10.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    // Reservas de una cancha en una fecha determinada
    List<Reserva> findByCanchaIdAndFecha(
            Long canchaId,
            LocalDate fecha
    );

    // Reservas de un usuario, ordenadas de la más reciente a la más antigua
    List<Reserva> findByUsuarioIdOrderByFechaDescHoraInicioDesc(
            Long usuarioId
    );

    // Reservas de una cancha y fecha, excluyendo una reserva
    // Se utiliza al modificar una reserva
    List<Reserva> findByCanchaIdAndFechaAndIdNot(
            Long canchaId,
            LocalDate fecha,
            Long reservaId
    );

    // Reservas de un usuario en una fecha
    List<Reserva> findByUsuarioIdAndFecha(
            Long usuarioId,
            LocalDate fecha
    );
}