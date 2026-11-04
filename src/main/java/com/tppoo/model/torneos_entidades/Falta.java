package com.tppoo.model.competencia;

public class Falta extends Evento {

    public Falta() {
        super();
    }

    public Falta(Jugador jugadorInvolucrado, Partido partido, int minuto) {
        super(jugadorInvolucrado, partido, minuto);
    }

    @Override
    public void procesar() {
        if (getJugadorInvolucrado() != null) {
            getJugadorInvolucrado().incrementarFaltasCometidas();
        }
    }
}