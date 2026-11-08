package com.tppoo.Modelo.Partido;

import com.tppoo.Modelo.Excepcion.EventoInvalidoException;
import com.tppoo.Modelo.Persona.Jugador;

public class Remate extends Evento {
    private final ZonaRemate zona;

    public Remate(Jugador jugadorInvolucrado, Partido partido, int minuto, ZonaRemate zona) {
        super(jugadorInvolucrado, partido, minuto);
        this.zona = zona;
    }

    @Override
    protected void aplicar() {
        if (getPartido() == null || getJugadorInvolucrado() == null) {
            throw new EventoInvalidoException("El remate debe tener un partido y un jugador asociados.");
        }
        if (zona == null) {
            throw new EventoInvalidoException("El remate debe tener una zona definida.");
        }
    }

    @Override
    protected void revertir() {
    }

    public double calcularXG() {
        return zona == null ? 0.0 : zona.getXg();
    }

    public ZonaRemate getZona() {
        return zona;
    }
}
