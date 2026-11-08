package com.tppoo.Modelo.Estadistica;

import com.tppoo.Modelo.Club.Club;

public class PosicionTabla implements Comparable<PosicionTabla> {
    private final Club club;
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

    public void registrarResultado(int golesAFavor, int golesEnContra) {
        this.partidosJugados++;
        this.golesAFavor += golesAFavor;
        this.golesEnContra += golesEnContra;

        if (golesAFavor > golesEnContra) {
            this.puntos += 3;
        } else if (golesAFavor == golesEnContra) {
            this.puntos += 1;
        }
    }

    public PosicionTabla copiar() {
        return new PosicionTabla(club, partidosJugados, puntos, golesAFavor, golesEnContra);
    }

    @Override
    public int compareTo(PosicionTabla otra) {
        if (puntos != otra.puntos) {
            return Integer.compare(otra.puntos, puntos);
        }
        if (calcularDiferenciaGoles() != otra.calcularDiferenciaGoles()) {
            return Integer.compare(otra.calcularDiferenciaGoles(), calcularDiferenciaGoles());
        }
        return Integer.compare(otra.golesAFavor, golesAFavor);
    }
}
