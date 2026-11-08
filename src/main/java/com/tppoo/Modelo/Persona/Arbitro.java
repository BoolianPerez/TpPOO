package com.tppoo.Modelo.Persona;

import com.tppoo.Modelo.Club.Club;

import java.time.LocalDate;
import java.util.List;

public class Arbitro extends Persona {
    private String colegio;
    private CategoriaArbitro categoria;
    private Club clubHincha;

    public Arbitro() {
    }

    public Arbitro(String dni, String nombre, String apellido, LocalDate fechaNacimiento,
                   String colegio, CategoriaArbitro categoria) {
        super(dni, nombre, apellido, fechaNacimiento);
        this.colegio = colegio;
        this.categoria = categoria;
    }

    @Override
    public String obtenerDetalle() {
        return "Arbitro: " + super.obtenerDetalle();
    }

    public boolean verificarDisponibilidad(LocalDate fechaPartido, Club local, Club visitante,
                                           List<LocalDate> fechasPartidosAsignados) {
        if (clubHincha != null && (clubHincha.equals(local) || clubHincha.equals(visitante))) {
            return false;
        }
        return fechasPartidosAsignados == null || !fechasPartidosAsignados.contains(fechaPartido);
    }

    public Club getClubHincha() {
        return clubHincha;
    }

    public void setClubHincha(Club clubHincha) {
        this.clubHincha = clubHincha;
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
