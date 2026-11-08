package com.tppoo.Modelo.Partido;

import com.tppoo.Modelo.Excepcion.EventoInvalidoException;
import com.tppoo.Modelo.Persona.Jugador;

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
