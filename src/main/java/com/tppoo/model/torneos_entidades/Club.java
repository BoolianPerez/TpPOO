package com.tppoo.model.torneos_entidades;

import com.tppoo.model.excepciones.ReglaNegocioException;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Club implements Serializable {
    private String nombre;
    private LocalDate fechaFundacion;
    private String sedeSocial;
    private Estadio estadioLocal;
    /** Jugadores con contrato vigente con el club (el plantel por torneo vive en InscripcionClub). */
    private final List<Jugador> jugadores = new ArrayList<>();
    private final List<Entrenador> entrenadores = new ArrayList<>();
    private final List<Patrocinio> patrocinios = new ArrayList<>();

    public Club() {
    }

    public Club(String nombre, LocalDate fechaFundacion, String sedeSocial) {
        this.nombre = nombre;
        this.fechaFundacion = fechaFundacion;
        this.sedeSocial = sedeSocial;
    }

    // ------------------------------------------------------------------ contratos

    public void incorporarJugador(Jugador jugador) {
        if (jugador == null) {
            throw new IllegalArgumentException("El jugador no puede ser null.");
        }
        if (!jugadores.contains(jugador)) {
            jugadores.add(jugador);
        }
    }

    /** Alta de un jugador con contrato. fin == null: sin vencimiento. */
    public Contrato contratar(Jugador jugador, LocalDate inicio, LocalDate fin) {
        if (jugador == null) {
            throw new IllegalArgumentException("El jugador no puede ser null.");
        }
        if (jugador.getContratoVigenteEn(inicio) != null) {
            throw new ReglaNegocioException("El jugador ya tiene un contrato vigente con otro club.");
        }
        Contrato contrato = new Contrato(this, inicio, fin);
        jugador.registrarPasoPorClub(contrato);
        incorporarJugador(jugador);
        return contrato;
    }

    /** Rescinde el contrato vigente: el jugador queda libre pero conserva historial y estadísticas (RN07). */
    public void rescindirContrato(Jugador jugador, LocalDate fecha) {
        Contrato vigente = jugador == null ? null : jugador.getContratoVigenteEn(fecha);
        if (vigente == null || !vigente.getClub().equals(this)) {
            throw new ReglaNegocioException("El jugador no tiene un contrato vigente con este club.");
        }
        vigente.rescindir(fecha);
        jugadores.remove(jugador);
    }

    /** Transferencia: rescinde acá y contrata en el destino desde el día siguiente. */
    public Contrato transferir(Jugador jugador, Club destino, LocalDate fecha, LocalDate finNuevoContrato) {
        if (destino == null || destino.equals(this)) {
            throw new IllegalArgumentException("El club destino debe ser otro club.");
        }
        rescindirContrato(jugador, fecha);
        return destino.contratar(jugador, fecha.plusDays(1), finNuevoContrato);
    }

    // ------------------------------------------------------------------ patrocinios

    public Sponsor obtenerSponsorPrincipal() {
        LocalDate hoy = LocalDate.now();
        for (Patrocinio patrocinio : patrocinios) {
            if (patrocinio.isEsPrincipal() && patrocinio.estaVigente(hoy)) {
                return patrocinio.getSponsor();
            }
        }
        return null;
    }

    /** RN06: valida superposición de períodos (no solo contra la fecha de hoy). */
    public void agregarPatrocinio(Patrocinio nuevoPatrocinio) {
        Patrocinio.validarNuevo(patrocinios, nuevoPatrocinio, true);
        patrocinios.add(nuevoPatrocinio);
    }

    // ------------------------------------------------------------------ equals / getters

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Club)) {
            return false;
        }
        Club otro = (Club) o;
        return nombre != null && nombre.equals(otro.nombre);
    }

    @Override
    public int hashCode() {
        return nombre == null ? System.identityHashCode(this) : nombre.hashCode();
    }

    public void agregarEntrenador(Entrenador entrenador) {
        if (entrenador != null && !entrenadores.contains(entrenador)) {
            entrenadores.add(entrenador);
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaFundacion() {
        return fechaFundacion;
    }

    public void setFechaFundacion(LocalDate fechaFundacion) {
        this.fechaFundacion = fechaFundacion;
    }

    public String getSedeSocial() {
        return sedeSocial;
    }

    public void setSedeSocial(String sedeSocial) {
        this.sedeSocial = sedeSocial;
    }

    public Estadio getEstadioLocal() {
        return estadioLocal;
    }

    public void setEstadioLocal(Estadio estadioLocal) {
        this.estadioLocal = estadioLocal;
    }

    public List<Jugador> getJugadores() {
        return Collections.unmodifiableList(jugadores);
    }

    public List<Entrenador> getEntrenadores() {
        return Collections.unmodifiableList(entrenadores);
    }

    public List<Patrocinio> getPatrocinios() {
        return Collections.unmodifiableList(patrocinios);
    }
}
