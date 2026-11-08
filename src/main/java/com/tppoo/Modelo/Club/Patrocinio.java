package com.tppoo.Modelo.Club;

import com.tppoo.Modelo.Excepcion.ReglaNegocioException;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class Patrocinio implements Serializable {
    private final double monto;
    private final LocalDate fechaInicio;
    private final LocalDate fechaFin;
    private final boolean esPrincipal;
    private final Sponsor sponsor;

    public Patrocinio(double monto, LocalDate fechaInicio, LocalDate fechaFin, boolean esPrincipal, Sponsor sponsor) {
        if (sponsor == null || fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("El patrocinio requiere sponsor y fechas de inicio y fin.");
        }
        if (fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior al inicio.");
        }
        if (monto < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo.");
        }
        this.monto = monto;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.esPrincipal = esPrincipal;
        this.sponsor = sponsor;
    }

    public boolean estaVigente(LocalDate fecha) {
        if (fecha == null) {
            return false;
        }
        return !fecha.isBefore(fechaInicio) && !fecha.isAfter(fechaFin);
    }

    public boolean seSuperponeCon(Patrocinio otro) {
        return !fechaFin.isBefore(otro.fechaInicio) && !otro.fechaFin.isBefore(fechaInicio);
    }

    public static void validarNuevo(List<Patrocinio> existentes, Patrocinio nuevo, boolean unicoPrincipal) {
        if (nuevo == null) {
            throw new IllegalArgumentException("El patrocinio no puede ser null.");
        }
        for (Patrocinio p : existentes) {
            if (!p.seSuperponeCon(nuevo)) {
                continue;
            }
            if (p.sponsor == nuevo.sponsor || p.sponsor.mismoSponsor(nuevo.sponsor)) {
                throw new ReglaNegocioException("El sponsor ya tiene un patrocinio vigente para este destinatario en ese período.");
            }
            if (unicoPrincipal && p.esPrincipal && nuevo.esPrincipal) {
                throw new ReglaNegocioException("El club ya cuenta con un sponsor principal vigente en ese período.");
            }
        }
    }

    public double getMonto() {
        return monto;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public boolean isEsPrincipal() {
        return esPrincipal;
    }

    public Sponsor getSponsor() {
        return sponsor;
    }
}
