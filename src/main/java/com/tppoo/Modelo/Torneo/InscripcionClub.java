package com.tppoo.Modelo.Torneo;

import com.tppoo.Modelo.Club.Club;
import com.tppoo.Modelo.Excepcion.PlantelInvalidoException;
import com.tppoo.Modelo.Excepcion.TransicionEstadoInvalidaException;
import com.tppoo.Modelo.Persona.Jugador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class InscripcionClub implements Serializable {

    private static final int MIN_JUGADORES = 18;
    private static final int MAX_JUGADORES = 30;

    private final Club club;
    private final Torneo torneo;
    private final List<Jugador> jugadores = new ArrayList<>();
    private final Map<String, Integer> amarillasAcumuladas = new HashMap<>();

    public InscripcionClub(Club club, Torneo torneo) {
        this.club = Objects.requireNonNull(club);
        this.torneo = Objects.requireNonNull(torneo);
    }

    public Club getClub() {
        return club;
    }

    public Torneo getTorneo() {
        return torneo;
    }

    public List<Jugador> getJugadores() {
        return new ArrayList<>(jugadores);
    }

    public void agregarJugador(Jugador jugador) {
        Objects.requireNonNull(jugador);

        if (torneo.getEstado() != EstadoTorneo.PLANIFICADO) {
            throw new TransicionEstadoInvalidaException("No se pueden modificar los planteles una vez iniciado el torneo.");
        }
        if (jugador.getDni() == null) {
            throw new IllegalArgumentException("El jugador necesita DNI para inscribirse.");
        }
        if (buscarJugador(jugador) != null) {
            throw new PlantelInvalidoException("El jugador ya pertenece al plantel de este club.");
        }
        if (jugadores.size() >= MAX_JUGADORES) {
            throw new PlantelInvalidoException("El plantel no puede superar los 30 jugadores.");
        }
        for (InscripcionClub inscripcion : torneo.getInscripciones()) {
            if (inscripcion != this && inscripcion.tieneJugador(jugador)) {
                throw new PlantelInvalidoException("El jugador ya está inscripto en otro club del torneo.");
            }
        }
        jugadores.add(jugador);
    }

    public void quitarJugador(Jugador jugador) {
        Objects.requireNonNull(jugador);

        if (torneo.getEstado() != EstadoTorneo.PLANIFICADO) {
            throw new TransicionEstadoInvalidaException("No se pueden modificar los planteles una vez iniciado el torneo.");
        }
        Jugador enPlantel = buscarJugador(jugador);
        if (enPlantel == null) {
            throw new PlantelInvalidoException("El jugador no pertenece a este plantel.");
        }
        jugadores.remove(enPlantel);
    }

    public int registrarAmarilla(Jugador jugador) {
        Objects.requireNonNull(jugador);
        if (!tieneJugador(jugador)) {
            throw new PlantelInvalidoException("El jugador no pertenece a este plantel.");
        }
        int cantidad = amarillasAcumuladas.getOrDefault(jugador.getDni(), 0) + 1;
        amarillasAcumuladas.put(jugador.getDni(), cantidad);
        return cantidad;
    }

    public void deshacerAmarilla(Jugador jugador) {
        int cantidad = amarillasAcumuladas.getOrDefault(jugador.getDni(), 0);
        if (cantidad > 0) {
            amarillasAcumuladas.put(jugador.getDni(), cantidad - 1);
        }
    }

    public int getAmarillasAcumuladas(Jugador jugador) {
        return jugador == null ? 0 : amarillasAcumuladas.getOrDefault(jugador.getDni(), 0);
    }

    public boolean tieneJugador(Jugador jugador) {
        return buscarJugador(jugador) != null;
    }

    private Jugador buscarJugador(Jugador jugador) {
        if (jugador == null) {
            return null;
        }
        for (Jugador existente : jugadores) {
            if (existente == jugador || existente.mismoDni(jugador)) {
                return existente;
            }
        }
        return null;
    }

    public boolean plantelValido() {
        return jugadores.size() >= MIN_JUGADORES && jugadores.size() <= MAX_JUGADORES;
    }
}
