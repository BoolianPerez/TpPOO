package com.tppoo.model.competencia

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Partido {
    private int marcadorVisitante;
    private Club clubVisitante;
    private int marcadorLocal;
    private Club clubLocal;
    private LocalDateTime fechaHora;
    private EstadoPartido estado;
    private Arbitro arbitroPrincipal;
    private Arbitro asistente1;
    private Arbitro asistente2;
    private Estadio estadio;
    private List<Evento> eventos = new ArrayList<>();

    public Partido() {
    }

    public void iniciar() {
        // TODO: inicializar partido.
    }

    public void registrarEvento(Evento evento) {
        this.eventos.add(evento);
    }

    public void finalizar() {
        // TODO: finalizar partido.
    }

    public int getMarcadorVisitante() {
        return marcadorVisitante;
    }

    public void setMarcadorVisitante(int marcadorVisitante) {
        this.marcadorVisitante = marcadorVisitante;
    }

    public Club getClubVisitante() {
        return clubVisitante;
    }

    public void setClubVisitante(Club clubVisitante) {
        this.clubVisitante = clubVisitante;
    }

    public int getMarcadorLocal() {
        return marcadorLocal;
    }

    public void setMarcadorLocal(int marcadorLocal) {
        this.marcadorLocal = marcadorLocal;
    }

    public Club getClubLocal() {
        return clubLocal;
    }

    public void setClubLocal(Club clubLocal) {
        this.clubLocal = clubLocal;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public EstadoPartido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPartido estado) {
        this.estado = estado;
    }

    public Arbitro getArbitroPrincipal() {
        return arbitroPrincipal;
    }

    public void setArbitroPrincipal(Arbitro arbitroPrincipal) {
        this.arbitroPrincipal = arbitroPrincipal;
    }

    public Arbitro getAsistente1() {
        return asistente1;
    }

    public void setAsistente1(Arbitro asistente1) {
        this.asistente1 = asistente1;
    }

    public Arbitro getAsistente2() {
        return asistente2;
    }

    public void setAsistente2(Arbitro asistente2) {
        this.asistente2 = asistente2;
    }

    public Estadio getEstadio() {
        return estadio;
    }

    public void setEstadio(Estadio estadio) {
        this.estadio = estadio;
    }

    public List<Evento> getEventos() {
        return eventos;
    }

    public void setEventos(List<Evento> eventos) {
        this.eventos = eventos;
    }
}