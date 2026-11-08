package com.tppoo.model.competencia;

import com.tppoo.model.torneos_entidades.Club;
import com.tppoo.model.torneos_entidades.EstadoPartido;
import com.tppoo.model.torneos_entidades.Evento;
import com.tppoo.model.torneos_entidades.Jugador;
import com.tppoo.model.torneos_entidades.Partido;
import com.tppoo.model.torneos_entidades.Remate;
import com.tppoo.model.torneos_entidades.Torneo;

/**
 * Calcula goles esperados (xG) a demanda; nunca se guardan en jugadores ni clubes (RN17).
 * Supuesto: el xG sale solo de los remates registrados (un gol sin remate asociado no suma xG).
 */
public class CalculadorXG {

    /** xG de un club en un partido: suma del xG de los remates de sus jugadores (ignora anulados). */
    public double calcularXG(Partido partido, Club club) {
        double xG = 0.0;
        for (Evento evento : partido.getEventos()) {
            if (evento instanceof Remate remate && !remate.estaAnulado()
                    && club.equals(partido.clubDe(remate.getJugadorInvolucrado()))) {
                xG += remate.calcularXG();
            }
        }
        return xG;
    }

    /** xG de un jugador en un partido. */
    public double calcularXGJugador(Partido partido, Jugador jugador) {
        double xG = 0.0;
        for (Evento evento : partido.getEventos()) {
            if (evento instanceof Remate remate && !remate.estaAnulado()
                    && jugador.equals(remate.getJugadorInvolucrado())) {
                xG += remate.calcularXG();
            }
        }
        return xG;
    }

    /** xG promedio por partido finalizado del club (solo cuentan los partidos que el club jugó). */
    public double calcularXGPromedio(Club club, Torneo torneo) {
        double total = 0.0;
        int partidos = 0;
        for (Partido partido : torneo.getPartidos()) {
            if (partido.getEstado() != EstadoPartido.FINALIZADO) {
                continue;
            }
            if (!club.equals(partido.getClubLocal()) && !club.equals(partido.getClubVisitante())) {
                continue;
            }
            total += calcularXG(partido, club);
            partidos++;
        }
        return partidos == 0 ? 0.0 : total / partidos;
    }
}
