package com.tppoo.model.torneos_entidades;

public class Cambio extends Evento {
    private Jugador jugadorEntra;
    private Jugador jugadorSale;

    public Cambio() {
        super();
    }

    public Cambio(Jugador jugadorInvolucrado, Partido partido, int minuto, Jugador jugadorEntra, Jugador jugadorSale) {
        super(jugadorInvolucrado, partido, minuto);
        this.jugadorEntra = jugadorEntra;
        this.jugadorSale = jugadorSale;
    }

    @Override
    public void procesar() {
        if (jugadorSale != null) {
            jugadorSale.setNoPuedeReingresar(true);
        }

        if (jugadorEntra != null) {
            jugadorEntra.setEnCancha(true);
        }
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