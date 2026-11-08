package com.tppoo.model.torneos_entidades;

import com.tppoo.model.excepciones.PlantelInvalidoException;
import com.tppoo.model.excepciones.TransicionEstadoInvalidaException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Participación de un club en un torneo, con su plantel y las amarillas acumuladas. */
public class InscripcionClub implements Serializable {

    private static final int MIN_JUGADORES = 18;
    private static final int MAX_JUGADORES = 30;

    private final Club club;
    private final Torneo torneo;
    private final List<Jugador> jugadores = new ArrayList<>();
    private final Map<Jugador, Integer> amarillasAcumuladas = new HashMap<>();

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
        if (jugadores.contains(jugador)) {
            throw new PlantelInvalidoException("El jugador ya pertenece al plantel de este club.");
        }
        if (jugadores.size() >= MAX_JUGADORES) {
            throw new PlantelInvalidoException("El plantel no puede superar los 30 jugadores.");
        }
        for (InscripcionClub inscripcion : torneo.getInscripciones()) {
            if (inscripcion.getClub() != club && inscripcion.tieneJugador(jugador)) {
                throw new PlantelInvalidoException("RN04: El jugador ya está inscripto en otro club del torneo.");
            }
        }
        jugadores.add(jugador);
    }

    /**
     * Quita un jugador mientras el torneo está planificado. El mínimo de 18 no se exige acá
     * (permite corregir cargas): se valida al iniciar el torneo en Torneo.avanzarEstado().
     */
    public void quitarJugador(Jugador jugador) {
        Objects.requireNonNull(jugador);

        if (torneo.getEstado() != EstadoTorneo.PLANIFICADO) {
            throw new TransicionEstadoInvalidaException("No se pueden modificar los planteles una vez iniciado el torneo.");
        }
        if (!jugadores.remove(jugador)) {
            throw new PlantelInvalidoException("El jugador no pertenece a este plantel.");
        }
    }

    /** Suma una amarilla y devuelve el total acumulado en el torneo. */
    public int registrarAmarilla(Jugador jugador) {
        Objects.requireNonNull(jugador);
        if (!tieneJugador(jugador)) {
            throw new PlantelInvalidoException("El jugador no pertenece a este plantel.");
        }
        int cantidad = amarillasAcumuladas.getOrDefault(jugador, 0) + 1;
        amarillasAcumuladas.put(jugador, cantidad);
        return cantidad;
    }

    /** Resta una amarilla (solo por anulación de la tarjeta). */
    void deshacerAmarilla(Jugador jugador) {
        int cantidad = amarillasAcumuladas.getOrDefault(jugador, 0);
        if (cantidad > 0) {
            amarillasAcumuladas.put(jugador, cantidad - 1);
        }
    }

    public int getAmarillasAcumuladas(Jugador jugador) {
        return amarillasAcumuladas.getOrDefault(jugador, 0);
    }

    public boolean tieneJugador(Jugador jugador) {
        return jugadores.contains(jugador);
    }

    public boolean plantelValido() {
        return jugadores.size() >= MIN_JUGADORES && jugadores.size() <= MAX_JUGADORES;
    }
}
