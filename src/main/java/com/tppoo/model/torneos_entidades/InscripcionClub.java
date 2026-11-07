package com.tppoo.model.torneos_entidades;

import java.util.*;

public class InscripcionClub {

    private static final int MIN_JUGADORES = 18;
    private static final int MAX_JUGADORES = 30;

    private Club club;
    private Torneo torneo;
    private List<Jugador> jugadores = new ArrayList<>();
    private Map<Jugador, Integer> amarillasAcumuladas = new HashMap<>();

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

        if (torneo.getEstado() != EstadoTorneo.PLANIFICADO) {
            throw new IllegalStateException(
                    "No se pueden modificar los planteles una vez iniciado el torneo."
            );
        }

        if (jugadores.contains(jugador)) {
            throw new IllegalArgumentException(
                    "El jugador ya pertenece al plantel de este club."
            );
        }

        if (jugadores.size() >= MAX_JUGADORES) {
            throw new IllegalStateException(
                    "El plantel no puede superar los 30 jugadores."
            );
        }

        for (InscripcionClub inscripcion : torneo.getInscripciones()) {

            if (inscripcion.getClub() != club &&
                    inscripcion.tieneJugador(jugador)) {

                throw new IllegalArgumentException(
                        "El jugador ya está inscripto en otro club del torneo."
                );
            }
        }

        jugadores.add(jugador);
    }

    public void quitarJugador(Jugador jugador) {

        Objects.requireNonNull(jugador);

        if (torneo.getEstado() != EstadoTorneo.PLANIFICADO) {
            throw new IllegalStateException(
                    "No se pueden modificar los planteles una vez iniciado el torneo."
            );
        }

        if (jugadores.size() <= MIN_JUGADORES) {
            throw new IllegalStateException(
                    "El plantel no puede tener menos de 18 jugadores."
            );
        }

        if (!jugadores.remove(jugador)) {
            throw new IllegalArgumentException(
                    "El jugador no pertenece a este plantel."
            );
        }
    }

    public int registrarAmarilla(Jugador jugador) {
        Objects.requireNonNull(jugador);

        if (!tieneJugador(jugador)) {
            throw new IllegalArgumentException(
                    "El jugador no pertenece a este plantel."
            );
        }

        int cantidad = amarillasAcumuladas.getOrDefault(jugador, 0) + 1;

        if (cantidad == 5) {
            amarillasAcumuladas.put(jugador, 0);
        } else {
            amarillasAcumuladas.put(jugador, cantidad);
        }

        return cantidad;
    }

    public int getAmarillasAcumuladas(Jugador jugador) {
        return amarillasAcumuladas.getOrDefault(jugador, 0);
    }

    public boolean tieneJugador(Jugador jugador) {
        return jugadores.contains(jugador);
    }

    public boolean plantelValido() {
        return jugadores.size() >= MIN_JUGADORES &&
                jugadores.size() <= MAX_JUGADORES;
    }
}