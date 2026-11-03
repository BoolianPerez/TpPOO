package com.tppoo.model.competencia

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Torneo {
    private EstadoTorneo estado;
    private String nombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private List<Club> clubes = new ArrayList<>();
    private List<Partido> partidos = new ArrayList<>();

    public Torneo() {
    }

    public void inscribirClub(Club club) {
        this.clubes.add(club);
    }

    public void generarFixture() {
        // TODO: generar calendario del torneo.
    }

    public void avanzarEstado() {
        // TODO: avanzar la fase del torneo.
    }

    public void generarPartido(Club clubLocal, Club clubVisitante, LocalDate fecha) {
        // TODO: crear partido del fixture.
    }

    public EstadoTorneo getEstado() {
        return estado;
    }

    public void setEstado(EstadoTorneo estado) {
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public List<Club> getClubes() {
        return clubes;
    }

    public void setClubes(List<Club> clubes) {
        this.clubes = clubes;
    }

    public List<Partido> getPartidos() {
        return partidos;
    }

    public void setPartidos(List<Partido> partidos) {
        this.partidos = partidos;
    }
}