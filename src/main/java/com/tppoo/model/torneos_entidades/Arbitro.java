package com.tppoo.model.torneos_entidades;

import java.time.LocalDate;
import java.util.List;

public class Arbitro extends Persona {
    private String colegio;
    private CategoriaArbitro categoria;
    private String clubHincha;


    public String getClubhincha() {
        return clubHincha;
    }

    public void setClubhincha(String clubhincha) {
        this.clubHincha = clubHincha;
    }

    public Arbitro() {
    }

    public Arbitro(String dni, String nombre, String apellido, LocalDate fechaNacimiento, String colegio, CategoriaArbitro categoria) {
        super(dni, nombre, apellido, fechaNacimiento);
        this.colegio = colegio;
        this.categoria = categoria;
        this.clubHincha = clubHincha;
    }

    @Override
    public String obtenerDetalle() {
        return "Arbitro: " + super.obtenerDetalle();
    }
    public boolean verificarDisponibilidad(LocalDate fechaPartido, String nombreClubOponente, List<LocalDate> fechasPartidosAsignados) {
        if (this.clubHincha != null && this.clubHincha.equalsIgnoreCase(nombreClubOponente)) {
            return false;
        }

        if (fechasPartidosAsignados != null && fechasPartidosAsignados.contains(fechaPartido)) {
            return false;
        }

        return true;
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
