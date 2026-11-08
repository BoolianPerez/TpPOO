package com.tppoo.model.torneos_entidades;

import com.tppoo.model.excepciones.EventoInvalidoException;

/** Cambio de jugadores. El jugador involucrado del evento es el que ENTRA. */
public class Cambio extends Evento {
    private final Jugador jugadorSale;

    public Cambio(Partido partido, int minuto, Jugador jugadorSale, Jugador jugadorEntra) {
        super(jugadorEntra, partido, minuto);
        this.jugadorSale = jugadorSale;
    }

    @Override
    protected void aplicar() {
        if (getPartido() == null || getJugadorEntra() == null || jugadorSale == null) {
            throw new EventoInvalidoException("El cambio debe tener partido, jugador entrante y jugador saliente.");
        }
        getPartido().registrarCambio(jugadorSale, getJugadorEntra());
    }

    @Override
    protected void revertir() {
        getPartido().deshacerCambio(jugadorSale, getJugadorEntra());
    }

    /** Las reglas de un cambio las valida Partido.registrarCambio (el que entra viene del banco). */
    @Override
    boolean exigeJugadorActivo() {
        return false;
    }

    public Jugador getJugadorEntra() {
        return getJugadorInvolucrado();
    }

    public Jugador getJugadorSale() {
        return jugadorSale;
    }
}
