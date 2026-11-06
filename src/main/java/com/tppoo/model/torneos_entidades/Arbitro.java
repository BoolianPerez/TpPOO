package com.tppoo.model.torneos_entidades;

import java.time.LocalDate;

public class Arbitro extends Persona {
    private String colegio;
    private CategoriaArbitro categoria;

    public Arbitro() {
    }

    public Arbitro(String dni, String nombre, String apellido, LocalDate fechaNacimiento, String colegio, CategoriaArbitro categoria) {
        super(dni, nombre, apellido, fechaNacimiento);
        this.colegio = colegio;
        this.categoria = categoria;
    }

    @Override
    public String obtenerDetalle() {
        return "Arbitro: " + super.obtenerDetalle();
    }

    public boolean verificarDisponibilidad(LocalDate fecha) {
        return true;
    }

    public String getColegio() {
        return colegio;
    }

    public void setColegio(String colegio) {
        this.colegio = colegio;
    }

    public CategoriaArbitro getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaArbitro categoria) {
        this.categoria = categoria;
    }
}