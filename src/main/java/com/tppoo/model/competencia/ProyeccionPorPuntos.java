package com.tppoo.model.competencia;

import com.tppoo.model.torneos_entidades.*;
import java.util.List;

public class ProyeccionPorPuntos implements EstrategiaProyeccion {

    private static final int PUNTAJE_CORTE = 45;

    @Override
    public double calcularProbabilidad(Torneo torneo, Club club) {

        ServicioReportes servicioReportes = new ServicioReportes();
        List<PosicionTabla> tabla =
                servicioReportes.calcularTablaPosiciones(torneo);

        PosicionTabla posicionClub = null;

        for (PosicionTabla posicion : tabla) {
            if (posicion.getClub().equals(club)) {
                posicionClub = posicion;
                break;
            }
        }

        if (posicionClub == null) {
            throw new IllegalArgumentException(
                    "El club no pertenece al torneo.");
        }

        int partidosTotales = 0;

        for (Partido partido : torneo.getPartidos()) {
            if (partido.getClubLocal().equals(club)
                    || partido.getClubVisitante().equals(club)) {
                partidosTotales++;
            }
        }

        int partidosJugados = posicionClub.getPartidosJugados();

        if (partidosJugados == 0 || partidosTotales == 0) {
            return 0.0;
        }

        double promedioPuntos =
                (double) posicionClub.getPuntos() / partidosJugados;

        double puntosProyectados = promedioPuntos * partidosTotales;

        return puntosProyectados >= PUNTAJE_CORTE ? 1.0 : 0.0;
    }
}