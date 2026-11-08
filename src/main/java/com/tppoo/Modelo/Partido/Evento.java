package com.tppoo.Modelo.Partido;

import com.tppoo.Modelo.Excepcion.EventoInvalidoException;
import com.tppoo.Modelo.Persona.Jugador;

import java.io.Serializable;

public abstract class Evento implements Serializable {
    private final Jugador jugadorInvolucrado;
    private final Partido partido;
    private final int minuto;
    private boolean procesado = false;
    private boolean anulado = false;

    protected Evento(Jugador jugadorInvolucrado, Partido partido, int minuto) {
        this.jugadorInvolucrado = jugadorInvolucrado;
        this.partido = partido;
        this.minuto = minuto;
    }

    public final void procesar() {
        if (procesado) {
            return;
        }
        aplicar();
        procesado = true;
    }

    protected abstract void aplicar();

    protected abstract void revertir();

    final void anular() {
        if (!procesado) {
            throw new EventoInvalidoException("No se puede anular un evento que no fue procesado.");
        }
        if (anulado) {
            throw new EventoInvalidoException("El evento ya fue anulado.");
        }
        revertir();
        anulado = true;
    }

    boolean exigeJugadorActivo() {
        return true;
    }

    void notificarA(ObservadorPartido observador) {
    }

    public Jugador getJugadorInvolucrado() {
        return jugadorInvolucrado;
    }

    public Partido getPartido() {
        return partido;
    }

    public int getMinuto() {
        return minuto;
    }

    public boolean estaProcesado() {
        return procesado;
    }

    public boolean estaAnulado() {
        return anulado;
    }
}
