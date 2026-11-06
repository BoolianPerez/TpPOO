package com.tppoo.model.competencia;

import com.tppoo.model.torneos_entidades.*;

import java.util.ArrayList;
import java.util.List;

public class ServicioProyecciones {
    private EstrategiaProyeccion estrategia;


    public ServicioProyecciones(EstrategiaProyeccion estrategia) {
        this.estrategia = estrategia;
    }

    public double calcularProbabilidadClasificacion(Torneo torneo, Club club) {
        return estrategia.calcularProbabilidad(torneo, club);
    }

    public double calcularXG(Partido partido, Club club, Torneo torneo) {
        double xG = 0.0;

        for (Evento evento : partido.getEventos()) {

            if (evento instanceof Remate remate) {

                Jugador jugador = remate.getJugadorInvolucrado();

                for (InscripcionClub inscripcion : torneo.getInscripciones()) {

                    if (inscripcion.getClub().equals(club) &&
                            inscripcion.tieneJugador(jugador)) {

                        xG += remate.calcularXG();
                        break;
                    }
                }
            }
        }

        return xG;
    }

    public double calcularXGPromedio(Club club, Torneo torneo) {

        double xGTotal = 0.0;
        int partidosJugados = 0;

        for (Partido partido : torneo.getPartidos()) {

            if (partido.getEstado() != EstadoPartido.FINALIZADO) {
                continue;
            }

            xGTotal += calcularXG(partido, club, torneo);
            partidosJugados++;
        }

        if (partidosJugados == 0) {
            return 0.0;
        }

        return xGTotal / partidosJugados;
    }



    public EstrategiaProyeccion getEstrategia() {
        return estrategia;
    }

    public void setEstrategia(EstrategiaProyeccion estrategia) {
        this.estrategia = estrategia;
    }
}