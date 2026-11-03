package com.tppoo.model.competencia

import java.time.LocalDate;

public class Patrocinio {
    private double monto;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Sponsor sponsor;

    public Patrocinio() {
    }

    public Patrocinio(double monto, LocalDate fechaInicio, LocalDate fechaFin, Sponsor sponsor) {
        this.monto = monto;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.sponsor = sponsor;
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

    public Sponsor getSponsor() {
        return sponsor;
    }

    public void setSponsor(Sponsor sponsor) {
        this.sponsor = sponsor;
    }
}