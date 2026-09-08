package com.generation.grupo10.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "canchas_servicios")
public class CanchaServicio {
    @EmbeddedId
    private CanchaServicioId id;

    @ManyToOne
    @MapsId("canchaId")
    @JoinColumn(name = "cancha_id")
    private Cancha cancha;

    @ManyToOne
    @MapsId("servicioId")
    @JoinColumn(name = "servicio_id")
    private Servicio servicio;

    public CanchaServicio(Cancha cancha, Servicio servicio) {
        this.cancha = cancha;
        this.servicio = servicio;
        this.id = new CanchaServicioId(cancha.getId(), servicio.getId());
    }
}
