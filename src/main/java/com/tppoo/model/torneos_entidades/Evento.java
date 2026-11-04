package com.tppoo.model.competencia;

public abstract class Evento {
    private Jugador jugadorInvolucrado;
    private Partido partido;
    private int minuto;

    public Evento() {
    }

    public Evento(Jugador jugadorInvolucrado, Partido partido, int minuto) {
        this.jugadorInvolucrado = jugadorInvolucrado;
        this.partido = partido;
        this.minuto = minuto;
    }

    public abstract void procesar();

    public Jugador getJugadorInvolucrado() {
        return jugadorInvolucrado;
    }

    public void setJugadorInvolucrado(Jugador jugadorInvolucrado) {
        this.jugadorInvolucrado = jugadorInvolucrado;
    }

    public Partido getPartido() {
        return partido;
    }

    public void setPartido(Partido partido) {
        this.partido = partido;
    }

    public int getMinuto() {
        return minuto;
    }

    public void setMinuto(int minuto) {
        this.minuto = minuto;
    }
}
}