package com.generation.grupo10.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CanchaServicioId implements Serializable {
    private Long canchaId;
    private Long servicioId;

    public CanchaServicioId() {
    }

    public CanchaServicioId(Long canchaId, Long servicioId) {
        this.canchaId = canchaId;
        this.servicioId = servicioId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof CanchaServicioId)) return false;

        CanchaServicioId that = (CanchaServicioId) o;

        return Objects.equals(canchaId, that.canchaId)
                && Objects.equals(servicioId, that.servicioId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(canchaId, servicioId);
    }

}
