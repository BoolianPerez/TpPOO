package com.tppoo.model.torneos_entidades;

import java.io.Serializable;
import java.util.Objects;

public class Sancion implements Serializable {

    private final Jugador jugador;
    private final Torneo torneo;
    private int partidosPendientes;
    private final String motivo;
    private final Partido partidoOrigen;

    public Sancion(Jugador jugador, Torneo torneo, int partidosPendientes, String motivo, Partido partidoOrigen) {
        this.jugador = Objects.requireNonNull(jugador);
        this.torneo = Objects.requireNonNull(torneo);
        this.partidoOrigen = Objects.requireNonNull(partidoOrigen);

        if (partidosPendientes <= 0) {
            throw new IllegalArgumentException("La sanción debe tener al menos un partido pendiente.");
        }
        this.partidosPendientes = partidosPendientes;
        this.motivo = Objects.requireNonNull(motivo);
    }

    public boolean estaVigente() {
        return partidosPendientes > 0;
    }

    public void cumplirPartido() {
        if (estaVigente()) {
            partidosPendientes--;
        }
    }

    public Jugador getJugador() {
        return jugador;
    }

    public Torneo getTorneo() {
        return torneo;
    }

    public int getPartidosPendientes() {
        return partidosPendientes;
    }

    public String getMotivo() {
        return motivo;
    }

    public Partido getPartidoOrigen() {
        return partidoOrigen;
    }
}
