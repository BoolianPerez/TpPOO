package com.tppoo.model.competencia;

import com.tppoo.model.torneos_entidades.Club;

public class PosicionTabla {
    private Club club;
    private int partidosJugados;
    private int puntos;
    private int golesAFavor;
    private int golesEnContra;

    public PosicionTabla(Club club, int partidosJugados, int puntos, int golesAFavor, int golesEnContra) {
        this.club = club;
        this.partidosJugados = partidosJugados;
        this.puntos = puntos;
        this.golesAFavor = golesAFavor;
        this.golesEnContra = golesEnContra;
    }

    public Club getClub() {
        return club;
    }

    public int getPartidosJugados() {
        return partidosJugados;
    }

    public int getPuntos() {
        return puntos;
    }

    public int getGolesAFavor() {
        return golesAFavor;
    }

    public int getGolesEnContra() {
        return golesEnContra;
    }

    public int calcularDiferenciaGoles() {
        return golesAFavor - golesEnContra;
    }

    public void registrarResultado(int golesAFavor, int golesEnContra){
        this.partidosJugados++;
        this.golesAFavor += golesAFavor;
        this.golesEnContra += golesEnContra;

        if (golesAFavor > golesEnContra) {
            this.puntos += 3;
        } else if (golesAFavor == golesEnContra) {
            this.puntos += 1;
        }
    }
}