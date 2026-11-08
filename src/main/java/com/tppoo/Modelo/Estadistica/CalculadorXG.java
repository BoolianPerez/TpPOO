package com.tppoo.Modelo.Estadistica;

import com.tppoo.Modelo.Club.Club;
import com.tppoo.Modelo.Partido.EstadoPartido;
import com.tppoo.Modelo.Partido.Evento;
import com.tppoo.Modelo.Persona.Jugador;
import com.tppoo.Modelo.Partido.Partido;
import com.tppoo.Modelo.Partido.Remate;
import com.tppoo.Modelo.Torneo.Torneo;


public class CalculadorXG {


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
