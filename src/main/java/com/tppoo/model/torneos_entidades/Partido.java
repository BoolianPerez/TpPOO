package com.tppoo.model.competencia;

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
        this.marcadorLocal = 0;
        this.marcadorVisitante = 0;
        this.estado = EstadoPartido.PROGRAMADO;
    }

    public void iniciar() {
        if (this.estado == EstadoPartido.PROGRAMADO) {
            this.estado = EstadoPartido.EN_JUEGO;
        } else {
            throw new IllegalStateException("El partido solo se puede iniciar si está en estado PROGRAMADO.");
        }
    }

    public void registrarEvento(Evento evento) {
        if (this.estado != EstadoPartido.EN_JUEGO) {
            throw new IllegalStateException("RN08: Los eventos únicamente pueden registrarse cuando el partido está EN_JUEGO.");
        }

        if (evento.getMinuto() < 0 || evento.getMinuto() > 120) {
            throw new IllegalArgumentException("RN08: El minuto del evento debe estar entre 0 y 120.");
        }

        this.eventos.add(evento);

        if (evento instanceof Gol) {
            Gol gol = (Gol) evento;
            if (gol.getClubAnotador().equals(this.clubLocal)) {
                this.marcadorLocal++;
            } else if (gol.getClubAnotador().equals(this.clubVisitante)) {
                this.marcadorVisitante++;
            }
        }
    }

    public void finalizar() {
        if (this.estado == EstadoPartido.EN_JUEGO) {
            this.estado = EstadoPartido.FINALIZADO;
        } else {
            throw new IllegalStateException("Solo se puede finalizar un partido que esté EN_JUEGO.");
        }
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