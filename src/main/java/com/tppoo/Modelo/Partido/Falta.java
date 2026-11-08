package com.tppoo.Modelo.Partido;

import com.tppoo.Modelo.Excepcion.EventoInvalidoException;
import com.tppoo.Modelo.Persona.Jugador;

public class Falta extends Evento {

    public Falta(Jugador jugadorInvolucrado, Partido partido, int minuto) {
        super(jugadorInvolucrado, partido, minuto);
    }

    @Override
    protected void aplicar() {
        if (getJugadorInvolucrado() == null || getPartido() == null) {
            throw new EventoInvalidoException("La falta debe tener un jugador y un partido asociados.");
        }
        getJugadorInvolucrado().incrementarFaltasCometidas();
    }

    @Override
    protected void revertir() {
        getJugadorInvolucrado().decrementarFaltasCometidas();
    }
}
