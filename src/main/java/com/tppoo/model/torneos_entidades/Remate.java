package com.tppoo.model.torneos_entidades;

import com.tppoo.model.excepciones.EventoInvalidoException;

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
        // El remate no modifica ningún contador: queda marcado como anulado y las proyecciones lo ignoran.
    }

    /** xG del remate según su zona (los valores viven en {@link ZonaRemate}). */
    public double calcularXG() {
        return zona == null ? 0.0 : zona.getXg();
    }

    public ZonaRemate getZona() {
        return zona;
    }
}
