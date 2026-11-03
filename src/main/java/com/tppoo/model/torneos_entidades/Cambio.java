package com.tppoo.model.competencia

public class Cambio extends Evento {
    private Jugador jugadorEntra;
    private Jugador jugadorSale;

    @Override
    public void procesar() {
        // TODO: aplicar logica de cambio.
    }

    public Jugador getJugadorEntra() {
        return jugadorEntra;
    }

    public void setJugadorEntra(Jugador jugadorEntra) {
        this.jugadorEntra = jugadorEntra;
    }

    public Jugador getJugadorSale() {
        return jugadorSale;
    }

    public void setJugadorSale(Jugador jugadorSale) {
        this.jugadorSale = jugadorSale;
    }
}