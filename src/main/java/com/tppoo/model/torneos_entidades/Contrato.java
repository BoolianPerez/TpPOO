package com.tppoo.model.torneos_entidades;

import java.time.LocalDate;

public class Contrato {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public Contrato() {
    }

    public Contrato(LocalDate fechaInicio, LocalDate fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }
    public boolean esVigenteEn(LocalDate fecha) {
        if (fechaInicio == null) return false;
        if (fechaFin == null) {
            return !fecha.isBefore(fechaInicio);
        }
        return (!fecha.isBefore(fechaInicio) && !fecha.isAfter(fechaFin));
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }
}
