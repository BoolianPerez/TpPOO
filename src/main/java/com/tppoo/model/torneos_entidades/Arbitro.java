package com.tppoo.model.torneos_entidades;

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

    /**
     * RN05: no puede dirigir a un club del que es hincha declarado ni arbitrar dos partidos el mismo día.
     */
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
