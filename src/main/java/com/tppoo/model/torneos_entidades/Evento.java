package com.tppoo.model.torneos_entidades;

import com.tppoo.model.excepciones.EventoInvalidoException;

import java.io.Serializable;

/**
 * Evento de un partido. Es inmutable una vez creado (RN11): los errores se corrigen
 * registrando una {@link Anulacion}, nunca borrando ni editando.
 *
 * Patrón Template Method: {@link #procesar()} es final y delega en {@link #aplicar()}.
 */
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

    /** Aplica el evento sobre el partido y los jugadores. Se ejecuta una sola vez. */
    public final void procesar() {
        if (procesado) {
            return;
        }
        aplicar();
        procesado = true;
    }

    /** Efecto propio de cada tipo de evento. */
    protected abstract void aplicar();

    /** Deshace el efecto de {@link #aplicar()} (lo usa la anulación). */
    protected abstract void revertir();

    /** Lo invoca únicamente {@link Anulacion}. */
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

    /** Si es true, el jugador involucrado debe estar en cancha y no expulsado (RN09). */
    boolean exigeJugadorActivo() {
        return true;
    }

    /** Cada evento decide si avisa al observador y cómo (polimorfismo). Por defecto no avisa. */
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

    public boolean estaProcesado() {
        return procesado;
    }

    protected void marcarProcesado() {
        this.procesado = true;
    }
}
