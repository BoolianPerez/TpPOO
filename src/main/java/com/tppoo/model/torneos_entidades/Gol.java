package com.tppoo.model.torneos_entidades;

import com.tppoo.model.excepciones.EventoInvalidoException;

public class Gol extends Evento {
    private final TipoGol tipo;
    private boolean sumoLocal;

    public Gol(Jugador jugadorInvolucrado, Partido partido, int minuto, TipoGol tipo) {
        super(jugadorInvolucrado, partido, minuto);
        this.tipo = tipo;
    }

    @Override
    protected void aplicar() {
        Partido partido = getPartido();
        Jugador jugador = getJugadorInvolucrado();

        if (partido == null || jugador == null || tipo == null) {
            throw new EventoInvalidoException("El gol debe tener partido, jugador y tipo.");
        }

        // El club del jugador se obtiene de su inscripción en el torneo (Experto).
        Club club = partido.clubDe(jugador);
        boolean esLocal = partido.getClubLocal().equals(club);
        boolean esVisitante = partido.getClubVisitante().equals(club);

        if (!esLocal && !esVisitante) {
            throw new EventoInvalidoException("El jugador no pertenece a ninguno de los clubes del partido.");
        }

        // Un gol en contra suma al rival.
        sumoLocal = (tipo != TipoGol.EN_CONTRA) == esLocal;

        if (sumoLocal) {
            partido.sumarGolLocal();
        } else {
            partido.sumarGolVisitante();
        }

        if (tipo != TipoGol.EN_CONTRA) {
            jugador.setGoles(jugador.getGoles() + 1);
        }
    }

    @Override
    protected void revertir() {
        if (sumoLocal) {
            getPartido().restarGolLocal();
        } else {
            getPartido().restarGolVisitante();
        }
        if (tipo != TipoGol.EN_CONTRA) {
            Jugador jugador = getJugadorInvolucrado();
            jugador.setGoles(jugador.getGoles() - 1);
        }
    }

    @Override
    void notificarA(ObservadorPartido observador) {
        observador.onGol(getPartido(), this);
    }

    public TipoGol getTipo() {
        return tipo;
    }
}
