package com.tppoo.model.competencia;

import com.tppoo.model.torneos_entidades.Club;
import com.tppoo.model.torneos_entidades.EstadoPartido;
import com.tppoo.model.torneos_entidades.Partido;
import com.tppoo.model.torneos_entidades.Torneo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Simula N veces los partidos restantes (goles ~ Poisson con el xG promedio de cada club)
 * y devuelve la fracción de simulaciones en que el club alcanza el puntaje de corte.
 * Todo lo costoso se calcula UNA vez antes del bucle.
 */
public class ProyeccionPorSimulacion implements EstrategiaProyeccion {
    public static final int SIMULACIONES_POR_DEFECTO = 10000;

    private final ServicioReportes servicioReportes;
    private final CalculadorXG calculadorXG;
    private final Random random;
    private final int puntajeCorte;
    private final int simulaciones;

    public ProyeccionPorSimulacion(ServicioReportes servicioReportes, CalculadorXG calculadorXG,
                                   Random random, int puntajeCorte) {
        this(servicioReportes, calculadorXG, random, puntajeCorte, SIMULACIONES_POR_DEFECTO);
    }

    public ProyeccionPorSimulacion(ServicioReportes servicioReportes, CalculadorXG calculadorXG,
                                   Random random, int puntajeCorte, int simulaciones) {
        if (simulaciones <= 0) {
            throw new IllegalArgumentException("La cantidad de simulaciones debe ser positiva.");
        }
        this.servicioReportes = servicioReportes;
        this.calculadorXG = calculadorXG;
        this.random = random;
        this.puntajeCorte = puntajeCorte;
        this.simulaciones = simulaciones;
    }

    @Override
    public double calcularProbabilidad(Torneo torneo, Club club) {
        List<PosicionTabla> base = servicioReportes.calcularTablaPosiciones(torneo);
        buscarPosicion(base, club); // valida que el club pertenezca al torneo

        List<Partido> pendientes = new ArrayList<>();
        Map<Club, Double> xgPromedio = new HashMap<>();
        for (Partido partido : torneo.getPartidos()) {
            if (partido.getEstado() == EstadoPartido.PROGRAMADO) {
                pendientes.add(partido);
                xgPromedio.computeIfAbsent(partido.getClubLocal(), c -> calculadorXG.calcularXGPromedio(c, torneo));
                xgPromedio.computeIfAbsent(partido.getClubVisitante(), c -> calculadorXG.calcularXGPromedio(c, torneo));
            }
        }

        int favorables = 0;
        for (int i = 0; i < simulaciones; i++) {
            List<PosicionTabla> tabla = new ArrayList<>();
            for (PosicionTabla p : base) {
                tabla.add(p.copiar());
            }
            for (Partido partido : pendientes) {
                int golesLocal = simularGoles(xgPromedio.get(partido.getClubLocal()));
                int golesVisitante = simularGoles(xgPromedio.get(partido.getClubVisitante()));
                buscarPosicion(tabla, partido.getClubLocal()).registrarResultado(golesLocal, golesVisitante);
                buscarPosicion(tabla, partido.getClubVisitante()).registrarResultado(golesVisitante, golesLocal);
            }
            if (buscarPosicion(tabla, club).getPuntos() >= puntajeCorte) {
                favorables++;
            }
        }
        return (double) favorables / simulaciones;
    }

    /** Muestreo de Poisson (algoritmo de Knuth). */
    private int simularGoles(double xG) {
        if (xG <= 0) {
            return 0;
        }
        double limite = Math.exp(-xG);
        int k = 0;
        double p = 1.0;
        do {
            k++;
            p *= random.nextDouble();
        } while (p > limite);
        return k - 1;
    }

    private PosicionTabla buscarPosicion(List<PosicionTabla> tabla, Club club) {
        for (PosicionTabla posicion : tabla) {
            if (posicion.getClub().equals(club)) {
                return posicion;
            }
        }
        throw new IllegalArgumentException("El club no pertenece al torneo.");
    }
}
