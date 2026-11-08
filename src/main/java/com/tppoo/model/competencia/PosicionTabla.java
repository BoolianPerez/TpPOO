package com.tppoo.model.competencia;

import com.tppoo.model.torneos_entidades.Club;

/**
 * Fila de la tabla de posiciones. Su orden natural (Comparable) es el de la tabla:
 * más puntos, mejor diferencia de gol y más goles a favor primero.
 */
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

    /** Copia independiente (la usa la simulación para no tocar la tabla real). */
    public PosicionTabla copiar() {
        return new PosicionTabla(club, partidosJugados, puntos, golesAFavor, golesEnContra);
    }

    /** Orden natural: la mejor posición va primero. 0 significa empate en los tres criterios. */
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
