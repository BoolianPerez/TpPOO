package com.tppoo.model

import java.util.ArrayList;
import java.util.List;

public class ModeloSistema {
    private final List<Club> clubes = new ArrayList<>();
    private final List<Torneo> torneos = new ArrayList<>();
    private final List<Partido> partidos = new ArrayList<>();

    public void agregarClub(Club club) {
        clubes.add(club);
    }

    public void agregarTorneo(Torneo torneo) {
        torneos.add(torneo);
    }

    public void agregarPartido(Partido partido) {
        partidos.add(partido);
    }

    public List<Club> getClubes() {
        return clubes;
    }

    public List<Torneo> getTorneos() {
        return torneos;
    }

    public List<Partido> getPartidos() {
        return partidos;
    }
}