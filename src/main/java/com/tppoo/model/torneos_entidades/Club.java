package com.tppoo.model.competencia;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Club {
    private String nombre;
    private LocalDate fechaFundacion;
    private String sedeSocial;
    private List<Jugador> jugadores = new ArrayList<>();
    private List<Entrenador> entrenadores = new ArrayList<>();
    private List<Patrocinio> patrocinios = new ArrayList<>();

    public Club() {
    }

    public Club(String nombre, LocalDate fechaFundacion, String sedeSocial) {
        this.nombre = nombre;
        this.fechaFundacion = fechaFundacion;
        this.sedeSocial = sedeSocial;
    }

    public void incorporarJugador(Jugador jugador) {
        this.jugadores.add(jugador);
    }

    public Sponsor obtenerSponsorPrincipal() {
        LocalDate hoy = LocalDate.now();
        for (Patrocinio patrocinio : patrocinios) {
            if (patrocinio.isEsPrincipal() && patrocinio.estaVigente(hoy)) {
                return patrocinio.getSponsor();
            }
        }
        return null;
    }

    public void agregarPatrocinio(Patrocinio nuevoPatrocinio) {
        if (nuevoPatrocinio.isEsPrincipal()) {
            LocalDate hoy = LocalDate.now();
            for (Patrocinio p : patrocinios) {
                if (p.isEsPrincipal() && p.estaVigente(hoy)) {
                    throw new IllegalStateException("RN06: El club ya cuenta con un sponsor principal vigente.");
                }
            }
        }
        this.patrocinios.add(nuevoPatrocinio);
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

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public List<Entrenador> getEntrenadores() {
        return entrenadores;
    }

    public void setEntrenadores(List<Entrenador> entrenadores) {
        this.entrenadores = entrenadores;
    }

    public List<Patrocinio> getPatrocinios() {
        return patrocinios;
    }

    public void setPatrocinios(List<Patrocinio> patrocinios) {
        this.patrocinios = patrocinios;
    }
}}