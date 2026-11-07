package com.tppoo.model.torneos_entidades;

public class Falta extends Evento {

    public Falta() {
        super();
    }

    public Falta(Jugador jugadorInvolucrado, Partido partido, int minuto) {
        super(jugadorInvolucrado, partido, minuto);
    }

    @Override
    public void procesar() {
        if (estaProcesado()) {
            return;
        }

        if (getJugadorInvolucrado() == null || getPartido() == null) {
            throw new IllegalStateException(
                    "La falta debe tener un jugador y un partido asociados."
            );
        }

        getJugadorInvolucrado().incrementarFaltasCometidas();

        marcarProcesado();
    }
}