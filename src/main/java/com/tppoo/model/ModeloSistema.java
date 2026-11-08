package com.tppoo.model;

import com.tppoo.model.torneos_entidades.Club;
import com.tppoo.model.torneos_entidades.Partido;
import com.tppoo.model.torneos_entidades.Torneo;

import com.tppoo.model.servicios.ServicioNotificaciones;
import com.tppoo.model.servicios.Suscriptor;
import com.tppoo.model.torneos_entidades.Arbitro;
import com.tppoo.model.torneos_entidades.Club;
import com.tppoo.model.torneos_entidades.Entrenador;
import com.tppoo.model.torneos_entidades.Estadio;
import com.tppoo.model.torneos_entidades.Jugador;
import com.tppoo.model.torneos_entidades.Partido;
import com.tppoo.model.torneos_entidades.Sponsor;
import com.tppoo.model.torneos_entidades.Torneo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Contenedor de todos los datos del sistema. Es lo que se guarda y carga de archivo. */
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
        agregarSinDuplicar(clubes, club);
    }

    public void agregarTorneo(Torneo torneo) {
        agregarSinDuplicar(torneos, torneo);
    }

    public void agregarEstadio(Estadio estadio) {
        agregarSinDuplicar(estadios, estadio);
    }

    public void agregarJugador(Jugador jugador) {
        agregarSinDuplicar(jugadores, jugador);
    }

    public void agregarArbitro(Arbitro arbitro) {
        agregarSinDuplicar(arbitros, arbitro);
    }

    public void agregarEntrenador(Entrenador entrenador) {
        agregarSinDuplicar(entrenadores, entrenador);
    }

    public void agregarSponsor(Sponsor sponsor) {
        agregarSinDuplicar(sponsors, sponsor);
    }

    public void agregarSuscriptor(Suscriptor suscriptor) {
        servicioNotificaciones.agregarSuscriptor(suscriptor);
    }

    private <T> void agregarSinDuplicar(List<T> lista, T elemento) {
        if (elemento == null) {
            throw new IllegalArgumentException("El elemento no puede ser null.");
        }
        if (!lista.contains(elemento)) {
            lista.add(elemento);
        }
    }

    /** Los partidos viven dentro de cada torneo; acá se consultan todos juntos. */
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
