package com.tppoo.model.torneos_entidades;

import java.util.Objects;

public class Sancion {

    private Jugador jugador;
    private Torneo torneo;
    private int partidosPendientes;
    private String motivo;

    public Sancion(Jugador jugador, Torneo torneo,
                   int partidosPendientes, String motivo) {
        this.jugador = Objects.requireNonNull(jugador);
        this.torneo = Objects.requireNonNull(torneo);
        this.partidosPendientes = partidosPendientes;
        this.motivo = motivo;
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
}
