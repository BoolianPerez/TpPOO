package com.tppoo.model.torneos_entidades;

import java.time.LocalDate;

public class Patrocinio {
    private double monto;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private boolean esPrincipal;
    private Sponsor sponsor;

    public Patrocinio() {
    }

    public Patrocinio(double monto, LocalDate fechaInicio, LocalDate fechaFin, boolean esPrincipal, Sponsor sponsor) {
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
        boolean despuesDeInicio = fecha.isEqual(fechaInicio) || fecha.isAfter(fechaInicio);
        boolean antesDeFin = fecha.isEqual(fechaFin) || fecha.isBefore(fechaFin);
        return despuesDeInicio && antesDeFin;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
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

    public boolean isEsPrincipal() {
        return esPrincipal;
    }

    public void setEsPrincipal(boolean esPrincipal) {
        this.esPrincipal = esPrincipal;
    }

    public Sponsor getSponsor() {
        return sponsor;
    }

    public void setSponsor(Sponsor sponsor) {
        this.sponsor = sponsor;
    }
}