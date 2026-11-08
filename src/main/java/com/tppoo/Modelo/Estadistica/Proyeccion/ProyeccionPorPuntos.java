package com.tppoo.Modelo.Estadistica.Proyeccion;

import com.tppoo.Modelo.Club.Club;
import com.tppoo.Modelo.Estadistica.PosicionTabla;
import com.tppoo.Modelo.Estadistica.ServicioReportes;
import com.tppoo.Modelo.Partido.EstadoPartido;
import com.tppoo.Modelo.Partido.Partido;
import com.tppoo.Modelo.Torneo.Torneo;

import java.util.List;

public class ProyeccionPorPuntos implements EstrategiaProyeccion {
    private final ServicioReportes servicioReportes;
    private final int puntajeCorte;

    public ProyeccionPorPuntos(ServicioReportes servicioReportes, int puntajeCorte) {
        this.servicioReportes = servicioReportes;
        this.puntajeCorte = puntajeCorte;
    }

    @Override
    public double calcularProbabilidad(Torneo torneo, Club club) {
        List<PosicionTabla> tabla = servicioReportes.calcularTablaPosiciones(torneo);

        PosicionTabla posicionClub = null;
        for (PosicionTabla posicion : tabla) {
            if (posicion.getClub().equals(club)) {
                posicionClub = posicion;
                break;
            }
        }
        if (posicionClub == null) {
            throw new IllegalArgumentException("El club no pertenece al torneo.");
        }

        int partidosTotales = 0;
        for (Partido partido : torneo.getPartidos()) {
            if (partido.getEstado() == EstadoPartido.SUSPENDIDO) {
                continue;
            }
            if (club.equals(partido.getClubLocal()) || club.equals(partido.getClubVisitante())) {
                partidosTotales++;
            }
        }

        int partidosJugados = posicionClub.getPartidosJugados();
        if (partidosJugados == 0 || partidosTotales == 0) {
            return 0.0;
        }

        double promedioPuntos = (double) posicionClub.getPuntos() / partidosJugados;
        double puntosProyectados = promedioPuntos * partidosTotales;
        return puntosProyectados >= puntajeCorte ? 1.0 : 0.0;
    }
}
