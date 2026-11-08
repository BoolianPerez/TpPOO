package com.tppoo.model.torneos_entidades;

import java.io.Serializable;
import java.time.LocalDate;

/** Paso de un jugador por un club. fechaFin == null significa contrato sin vencimiento. */
public class Contrato implements Serializable {
    private final Club club;
    private final LocalDate fechaInicio;
    private LocalDate fechaFin;

    public Contrato(Club club, LocalDate fechaInicio, LocalDate fechaFin) {
        if (club == null || fechaInicio == null) {
            throw new IllegalArgumentException("El contrato requiere club y fecha de inicio.");
        }
        if (fechaFin != null && fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior al inicio.");
        }
        this.club = club;
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

    public boolean esVigenteEn(LocalDate fecha) {
        if (fecha == null) {
            return false;
        }
        if (fecha.isBefore(fechaInicio)) {
            return false;
        }
        return fechaFin == null || !fecha.isAfter(fechaFin);
    }

    /** Rescinde el contrato: el último día de vigencia pasa a ser la fecha indicada. */
    public void rescindir(LocalDate fecha) {
        if (fecha == null || fecha.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("La fecha de rescisión no puede ser anterior al inicio del contrato.");
        }
        this.fechaFin = fecha;
    }

    public Club getClub() {
        return club;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }
}
