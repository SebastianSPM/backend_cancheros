package com.generation.grupo10.service;



import com.generation.grupo10.dto.ReservaRequest;
import com.generation.grupo10.dto.ReservaResponse;
import com.generation.grupo10.exception.ResourceNotFoundException;
import com.generation.grupo10.model.Cancha;
import com.generation.grupo10.model.EstadoReserva;
import com.generation.grupo10.model.Reserva;
import com.generation.grupo10.repository.CanchaRepository;
import com.generation.grupo10.repository.ReservaRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final CanchaRepository canchaRepository;


    public ReservaResponse crearReserva(ReservaRequest request) {

        // 1. Buscar cancha
        Cancha cancha = canchaRepository.findById(request.getCanchaId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cancha no encontrada"
                        )
                );


        // 2. Validar duración
        if (request.getDuracion() == null ||
                request.getDuracion() < 1 ||
                request.getDuracion() > 3) {

            throw new IllegalArgumentException(
                    "La duración debe ser de 1, 2 o 3 horas"
            );
        }


        // 3. Validar fecha
        if (request.getFecha().isBefore(LocalDate.now())) {

            throw new IllegalArgumentException(
                    "No se puede reservar una fecha pasada"
            );
        }


        // 4. Calcular hora final
        LocalTime horaFin = request.getHoraInicio()
                .plusHours(request.getDuracion());


        // 5. Verificar disponibilidad
        validarDisponibilidad(
                cancha.getId(),
                request.getFecha(),
                request.getHoraInicio(),
                horaFin
        );


        // 6. Calcular total
        Double total =
                cancha.getPrecioHora() * request.getDuracion();


        // 7. Crear reserva
        Reserva reserva = new Reserva();

        reserva.setCancha(cancha);
        reserva.setNombreCompleto(
                request.getNombreCompleto()
        );
        reserva.setCorreo(
                request.getCorreo()
        );
        reserva.setTelefono(
                request.getTelefono()
        );
        reserva.setFecha(
                request.getFecha()
        );
        reserva.setHoraInicio(
                request.getHoraInicio()
        );
        reserva.setDuracion(
                request.getDuracion()
        );
        reserva.setTotal(total);

        reserva.setEstado(
                EstadoReserva.CONFIRMADA
        );


        // 8. Guardar
        Reserva reservaGuardada =
                reservaRepository.save(reserva);


        // 9. Convertir a Response
        return convertirResponse(reservaGuardada);
    }


    private void validarDisponibilidad(
            Long canchaId,
            LocalDate fecha,
            LocalTime horaInicio,
            LocalTime horaFin) {

        List<Reserva> reservas =
                reservaRepository.findByCanchaIdAndFecha(
                        canchaId,
                        fecha
                );


        for (Reserva reserva : reservas) {

            if (reserva.getEstado() ==
                    EstadoReserva.CANCELADA) {

                continue;
            }


            LocalTime reservaInicio =
                    reserva.getHoraInicio();

            LocalTime reservaFin =
                    reservaInicio.plusHours(
                            reserva.getDuracion()
                    );


            boolean existeConflicto =
                    horaInicio.isBefore(reservaFin)
                            &&
                            horaFin.isAfter(reservaInicio);


            if (existeConflicto) {

                throw new IllegalArgumentException(
                        "La cancha ya está reservada en ese horario"
                );
            }
        }
    }


    private ReservaResponse convertirResponse(
            Reserva reserva) {

        LocalTime horaFin =
                reserva.getHoraInicio()
                        .plusHours(
                                reserva.getDuracion()
                        );


        return new ReservaResponse(

                reserva.getId(),

                reserva.getCancha().getId(),

                reserva.getCancha().getNombre(),

                reserva.getNombreCompleto(),

                reserva.getCorreo(),

                reserva.getTelefono(),

                reserva.getFecha(),

                reserva.getHoraInicio(),

                horaFin,

                reserva.getDuracion(),

                reserva.getTotal(),

                reserva.getEstado()
        );
    }
}