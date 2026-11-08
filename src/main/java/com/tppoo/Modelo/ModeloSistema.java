package com.tppoo.Modelo;

import com.tppoo.Modelo.Excepcion.ReglaNegocioException;
import com.tppoo.Modelo.Notificacion.ServicioNotificaciones;
import com.tppoo.Modelo.Notificacion.Suscriptor;
import com.tppoo.Modelo.Persona.Arbitro;
import com.tppoo.Modelo.Club.Club;
import com.tppoo.Modelo.Persona.Entrenador;
import com.tppoo.Modelo.Club.Estadio;
import com.tppoo.Modelo.Persona.Jugador;
import com.tppoo.Modelo.Partido.Partido;
import com.tppoo.Modelo.Persona.Persona;
import com.tppoo.Modelo.Club.Sponsor;
import com.tppoo.Modelo.Torneo.Torneo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ModeloSistema implements Serializable {
    private final List<Club> clubes = new ArrayList<>();
    private final List<Torneo> torneos = new ArrayList<>();
    private final List<Estadio> estadios = new ArrayList<>();
    private final List<Jugador> jugadores = new ArrayList<>();
    private final List<Arbitro> arbitros = new ArrayList<>();
    private final List<Entrenador> entrenadores = new ArrayList<>();
    private final List<Sponsor> sponsors = new ArrayList<>();
    private final ServicioNotificaciones servicioNotificaciones = new ServicioNotificaciones();

    public void agregarClub(Club club) {
        exigir(club, "club");
        if (buscarClubPorNombre(club.getNombre()) != null) {
            throw new ReglaNegocioException("Ya existe un club con el nombre " + club.getNombre() + ".");
        }
        clubes.add(club);
    }

    public void agregarTorneo(Torneo torneo) {
        exigir(torneo, "torneo");
        if (buscarTorneoPorNombre(torneo.getNombre()) != null) {
            throw new ReglaNegocioException("Ya existe un torneo con el nombre " + torneo.getNombre() + ".");
        }
        torneos.add(torneo);
    }

    public void agregarEstadio(Estadio estadio) {
        exigir(estadio, "estadio");
        if (buscarEstadioPorNombre(estadio.getNombre()) != null) {
            throw new ReglaNegocioException("Ya existe un estadio con el nombre " + estadio.getNombre() + ".");
        }
        estadios.add(estadio);
    }

    public void agregarSponsor(Sponsor sponsor) {
        exigir(sponsor, "sponsor");
        for (Sponsor existente : sponsors) {
            if (existente == sponsor || existente.mismoSponsor(sponsor)) {
                throw new ReglaNegocioException("Ya existe el sponsor " + sponsor.getNombre() + " (" + sponsor.getMarca() + ").");
            }
        }
        sponsors.add(sponsor);
    }

    public void agregarJugador(Jugador jugador) {
        agregarPersona(jugadores, jugador, "jugador");
    }

    public void agregarArbitro(Arbitro arbitro) {
        agregarPersona(arbitros, arbitro, "árbitro");
    }

    public void agregarEntrenador(Entrenador entrenador) {
        agregarPersona(entrenadores, entrenador, "entrenador");
    }

    public void agregarSuscriptor(Suscriptor suscriptor) {
        servicioNotificaciones.agregarSuscriptor(suscriptor); // rechaza nombre repetido
    }

    private <T extends Persona> void agregarPersona(List<T> lista, T persona, String tipo) {
        exigir(persona, tipo);
        if (persona.getDni() == null || persona.getDni().isBlank()) {
            throw new IllegalArgumentException("El " + tipo + " necesita DNI.");
        }
        for (T existente : lista) {
            if (existente == persona || existente.mismoDni(persona)) {
                throw new ReglaNegocioException("Ya existe un " + tipo + " con el DNI " + persona.getDni() + ".");
            }
        }
        lista.add(persona);
    }

    private void exigir(Object elemento, String tipo) {
        if (elemento == null) {
            throw new IllegalArgumentException("El " + tipo + " no puede ser null.");
        }
    }

    public Club buscarClubPorNombre(String nombre) {
        for (Club club : clubes) {
            if (Objects.equals(club.getNombre(), nombre)) {
                return club;
            }
        }
        return null;
    }

    public Torneo buscarTorneoPorNombre(String nombre) {
        for (Torneo torneo : torneos) {
            if (Objects.equals(torneo.getNombre(), nombre)) {
                return torneo;
            }
        }
        return null;
    }

    public Estadio buscarEstadioPorNombre(String nombre) {
        for (Estadio estadio : estadios) {
            if (Objects.equals(estadio.getNombre(), nombre)) {
                return estadio;
            }
        }
        return null;
    }

    public Jugador buscarJugadorPorDni(String dni) {
        return buscarPorDni(jugadores, dni);
    }

    public Arbitro buscarArbitroPorDni(String dni) {
        return buscarPorDni(arbitros, dni);
    }

    public Entrenador buscarEntrenadorPorDni(String dni) {
        return buscarPorDni(entrenadores, dni);
    }

    private <T extends Persona> T buscarPorDni(List<T> lista, String dni) {
        for (T persona : lista) {
            if (dni != null && dni.equals(persona.getDni())) {
                return persona;
            }
        }
        return null;
    }

    public List<Partido> getPartidos() {
        List<Partido> todos = new ArrayList<>();
        for (Torneo torneo : torneos) {
            todos.addAll(torneo.getPartidos());
        }
        return todos;
    }

    public List<Club> getClubes() {
        return Collections.unmodifiableList(clubes);
    }

    public List<Torneo> getTorneos() {
        return Collections.unmodifiableList(torneos);
    }

    public List<Estadio> getEstadios() {
        return Collections.unmodifiableList(estadios);
    }

    public List<Jugador> getJugadores() {
        return Collections.unmodifiableList(jugadores);
    }

    public List<Arbitro> getArbitros() {
        return Collections.unmodifiableList(arbitros);
    }

    public List<Entrenador> getEntrenadores() {
        return Collections.unmodifiableList(entrenadores);
    }

    public List<Sponsor> getSponsors() {
        return Collections.unmodifiableList(sponsors);
    }

    public List<Suscriptor> getSuscriptores() {
        return servicioNotificaciones.getSuscriptores();
    }

    public ServicioNotificaciones getServicioNotificaciones() {
        return servicioNotificaciones;
    }
}
