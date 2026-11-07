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
        if (estaProcesado()) {
            return;
        }

        Partido partido = getPartido();

        if (partido == null || jugadorEntra == null || jugadorSale == null) {
            throw new IllegalStateException(
                    "El cambio debe tener partido, jugador entrante y jugador saliente."
            );
        }

        partido.registrarCambio(jugadorSale, jugadorEntra);

        marcarProcesado();
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