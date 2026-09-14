package com.generation.grupo10.service;

import com.generation.grupo10.dto.DisponibilidadResponse;
import com.generation.grupo10.dto.ReservaRequest;
import com.generation.grupo10.dto.ReservaResponse;
import com.generation.grupo10.dto.ReservaResumen;
import com.generation.grupo10.exception.ResourceNotFoundException;
import com.generation.grupo10.model.Cancha;
import com.generation.grupo10.model.EstadoReserva;
import com.generation.grupo10.model.Reserva;
import com.generation.grupo10.model.Usuario;
import com.generation.grupo10.repository.CanchaRepository;
import com.generation.grupo10.repository.ReservaRepository;
import com.generation.grupo10.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final CanchaRepository canchaRepository;
    private final UsuarioRepository usuarioRepository;


    // =========================================================
    // CREAR RESERVA
    // =========================================================

    public ReservaResponse crearReserva(
            Long usuarioId,
            ReservaRequest request) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Usuario no encontrado"
                        )
                );

        Cancha cancha = buscarCancha(request.getCanchaId());

        validarDatosReserva(request);

        LocalTime horaFin =
                request.getHoraInicio()
                        .plusHours(request.getDuracion());

        validarHorario(horaFin);

        validarDisponibilidad(
                cancha.getId(),
                request.getFecha(),
                request.getHoraInicio(),
                horaFin
        );

        BigDecimal total =
                calcularTotal(
                        cancha.getPrecioPorHora(),
                        request.getDuracion()
                );

        Reserva reserva = new Reserva();

        reserva.setUsuario(usuario);
        reserva.setCancha(cancha);
        reserva.setFecha(request.getFecha());
        reserva.setHoraInicio(request.getHoraInicio());
        reserva.setDuracion(request.getDuracion());
        reserva.setPrecioHora(cancha.getPrecioPorHora());
        reserva.setTotal(total);
        reserva.setEstado(EstadoReserva.CONFIRMADA);

        Reserva guardada =
                reservaRepository.save(reserva);

        return convertirResponse(guardada);
    }


    // =========================================================
    // MIS RESERVAS
    // =========================================================

    @Transactional(readOnly = true)
    public List<ReservaResumen> obtenerMisReservas(
            Long usuarioId) {

        return reservaRepository
                .findByUsuarioIdOrderByFechaDescHoraInicioDesc(
                        usuarioId
                )
                .stream()
                .map(this::convertirResumen)
                .toList();
    }


    // =========================================================
    // DETALLE DE RESERVA
    // =========================================================

    @Transactional(readOnly = true)
    public ReservaResponse obtenerPorId(
            Long reservaId,
            Long usuarioId) {

        Reserva reserva = buscarReserva(reservaId);

        validarPropietario(reserva, usuarioId);

        return convertirResponse(reserva);
    }


    // =========================================================
    // MODIFICAR RESERVA
    // =========================================================

    public ReservaResponse actualizarReserva(
            Long reservaId,
            Long usuarioId,
            ReservaRequest request) {

        Reserva reserva =
                buscarReserva(reservaId);

        validarPropietario(
                reserva,
                usuarioId
        );

        if (reserva.getEstado() ==
                EstadoReserva.CANCELADA) {

            throw new IllegalArgumentException(
                    "No se puede modificar una reserva cancelada"
            );
        }

        Cancha cancha =
                buscarCancha(request.getCanchaId());

        validarDatosReserva(request);

        LocalTime horaFin =
                request.getHoraInicio()
                        .plusHours(request.getDuracion());

        validarHorario(horaFin);

        validarDisponibilidadExcluyendoReserva(
                cancha.getId(),
                request.getFecha(),
                request.getHoraInicio(),
                horaFin,
                reservaId
        );

        BigDecimal total =
                calcularTotal(
                        cancha.getPrecioPorHora(),
                        request.getDuracion()
                );

        reserva.setCancha(cancha);
        reserva.setFecha(request.getFecha());
        reserva.setHoraInicio(request.getHoraInicio());
        reserva.setDuracion(request.getDuracion());
        reserva.setPrecioHora(cancha.getPrecioPorHora());
        reserva.setTotal(total);

        Reserva actualizada =
                reservaRepository.save(reserva);

        return convertirResponse(actualizada);
    }


    // =========================================================
    // CANCELAR
    // =========================================================

    public void cancelarReserva(
            Long reservaId,
            Long usuarioId) {

        Reserva reserva =
                buscarReserva(reservaId);

        validarPropietario(
                reserva,
                usuarioId
        );

        if (reserva.getEstado() ==
                EstadoReserva.CANCELADA) {

            throw new IllegalArgumentException(
                    "La reserva ya está cancelada"
            );
        }

        reserva.setEstado(
                EstadoReserva.CANCELADA
        );

        reservaRepository.save(reserva);
    }


    // =========================================================
    // DISPONIBILIDAD
    // =========================================================

    @Transactional(readOnly = true)
    public List<DisponibilidadResponse> consultarDisponibilidad(
            Long canchaId,
            LocalDate fecha) {

        Cancha cancha =
                buscarCancha(canchaId);

        if (!Boolean.TRUE.equals(cancha.getDisponible())) {

            throw new IllegalArgumentException(
                    "La cancha no está disponible"
            );
        }

        if (fecha.isBefore(LocalDate.now())) {

            throw new IllegalArgumentException(
                    "No se puede consultar una fecha pasada"
            );
        }

        List<Reserva> reservas =
                reservaRepository.findByCanchaIdAndFecha(
                        canchaId,
                        fecha
                );

        List<DisponibilidadResponse> horarios =
                new ArrayList<>();

        LocalTime horaActual =
                LocalTime.of(8, 0);

        LocalTime horaCierre =
                LocalTime.of(23, 0);

        while (horaActual.isBefore(horaCierre)) {

            LocalTime horaFin =
                    horaActual.plusHours(1);

            boolean disponible = true;

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

                boolean conflicto =
                        horaActual.isBefore(reservaFin)
                                &&
                                horaFin.isAfter(reservaInicio);

                if (conflicto) {

                    disponible = false;
                    break;
                }
            }

            // Si la fecha es hoy,
            // las horas que ya pasaron no están disponibles.
            if (fecha.equals(LocalDate.now())
                    && !horaActual.isAfter(LocalTime.now())) {

                disponible = false;
            }

            horarios.add(
                    new DisponibilidadResponse(
                            horaActual,
                            horaFin,
                            disponible
                    )
            );

            horaActual = horaFin;
        }

        return horarios;
    }


    // =========================================================
    // VALIDACIONES
    // =========================================================

    private void validarDatosReserva(
            ReservaRequest request) {

        if (request.getFecha() == null) {

            throw new IllegalArgumentException(
                    "La fecha es obligatoria"
            );
        }

        if (request.getHoraInicio() == null) {

            throw new IllegalArgumentException(
                    "La hora de inicio es obligatoria"
            );
        }

        if (request.getDuracion() == null
                || request.getDuracion() < 1
                || request.getDuracion() > 3) {

            throw new IllegalArgumentException(
                    "La duración debe ser de 1, 2 o 3 horas"
            );
        }

        if (request.getFecha()
                .isBefore(LocalDate.now())) {

            throw new IllegalArgumentException(
                    "No se puede reservar una fecha pasada"
            );
        }

        if (request.getFecha()
                .equals(LocalDate.now())
                && request.getHoraInicio()
                .isBefore(LocalTime.now())) {

            throw new IllegalArgumentException(
                    "No se puede reservar una hora que ya pasó"
            );
        }
    }


    private void validarHorario(
            LocalTime horaFin) {

        LocalTime apertura =
                LocalTime.of(8, 0);

        LocalTime cierre =
                LocalTime.of(23, 0);

        if (horaFin.isAfter(cierre)) {

            throw new IllegalArgumentException(
                    "La cancha funciona hasta las 23:00"
            );
        }
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

        comprobarConflicto(
                reservas,
                horaInicio,
                horaFin
        );
    }


    private void validarDisponibilidadExcluyendoReserva(
            Long canchaId,
            LocalDate fecha,
            LocalTime horaInicio,
            LocalTime horaFin,
            Long reservaId) {

        List<Reserva> reservas =
                reservaRepository
                        .findByCanchaIdAndFechaAndIdNot(
                                canchaId,
                                fecha,
                                reservaId
                        );

        comprobarConflicto(
                reservas,
                horaInicio,
                horaFin
        );
    }


    private void comprobarConflicto(
            List<Reserva> reservas,
            LocalTime horaInicio,
            LocalTime horaFin) {

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

            boolean conflicto =
                    horaInicio.isBefore(reservaFin)
                            &&
                            horaFin.isAfter(reservaInicio);

            if (conflicto) {

                throw new IllegalArgumentException(
                        "La cancha ya está reservada en ese horario"
                );
            }
        }
    }


    // =========================================================
    // MÉTODOS AUXILIARES
    // =========================================================

    private Cancha buscarCancha(Long canchaId) {

        return canchaRepository.findById(canchaId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cancha no encontrada"
                        )
                );
    }


    private Reserva buscarReserva(Long reservaId) {

        return reservaRepository.findById(reservaId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Reserva no encontrada"
                        )
                );
    }


    private void validarPropietario(
            Reserva reserva,
            Long usuarioId) {

        if (!reserva.getUsuario()
                .getId()
                .equals(usuarioId)) {

            throw new IllegalArgumentException(
                    "No tienes permiso para modificar esta reserva"
            );
        }
    }


    private BigDecimal calcularTotal(
            BigDecimal precioHora,
            Integer duracion) {

        return precioHora.multiply(
                BigDecimal.valueOf(duracion)
        );
    }


    // =========================================================
    // CONVERSIONES
    // =========================================================

    private ReservaResponse convertirResponse(
            Reserva reserva) {

        LocalTime horaFin =
                reserva.getHoraInicio()
                        .plusHours(
                                reserva.getDuracion()
                        );

        Usuario usuario =
                reserva.getUsuario();

        String nombreCompleto =
                usuario.getNombre()
                        + " "
                        + usuario.getApellido();

        return new ReservaResponse(

                reserva.getId(),

                usuario.getId(),

                reserva.getCancha().getId(),

                reserva.getCancha().getNombreCancha(),

                nombreCompleto,

                usuario.getEmail(),

                usuario.getTelefono(),

                reserva.getFecha(),

                reserva.getHoraInicio(),

                horaFin,

                reserva.getDuracion(),

                reserva.getPrecioHora(),

                reserva.getTotal(),

                reserva.getEstado()
        );
    }


    private ReservaResumen convertirResumen(
            Reserva reserva) {

        LocalTime horaFin =
                reserva.getHoraInicio()
                        .plusHours(
                                reserva.getDuracion()
                        );

        return new ReservaResumen(

                reserva.getId(),

                reserva.getCancha().getNombreCancha(),

                reserva.getFecha(),

                reserva.getHoraInicio(),

                horaFin,

                reserva.getDuracion(),

                reserva.getTotal(),

                reserva.getEstado()
        );
    }
}