package com.tppoo.model.competencia;

import com.tppoo.model.torneos_entidades.*;

import java.util.ArrayList;
import java.util.List;

public class ProyeccionPorSimulacion implements EstrategiaProyeccion {
    private ServicioProyecciones servicioProyecciones;

    private static final int CANTIDAD_SIMULACIONES = 10000;

    public ProyeccionPorSimulacion(ServicioProyecciones servicioProyecciones) {
        this.servicioProyecciones = servicioProyecciones;
    }

    @Override
    public double calcularProbabilidad(Torneo torneo, Club club) {

        int favorables = 0;

        for (int i = 0; i < CANTIDAD_SIMULACIONES; i++) {

            List<PosicionTabla> tabla = copiarTabla(torneo);

            ejecutarSimulacion(torneo, tabla);

            PosicionTabla posicion = buscarPosicion(tabla, club);

            if (posicion.getPuntos() >= PUNTAJE_CORTE) {
                favorables++;
            }
        }

        return (double) favorables / CANTIDAD_SIMULACIONES;
    }

    private int simularGoles(double xG) {

        if (xG <= 0){
            return 0;
        }

        double L = Math.exp(-xG);
        int k = 0;
        double p = 1.0;

        do {
            k++;
            p *= Math.random();
        } while (p > L);

        return k - 1;
    }

    private int[] simularPartido(double xGLocal, double xGVisitante) {

        int golesLocal = simularGoles(xGLocal);
        int golesVisitante = simularGoles(xGVisitante);

        return new int[]{golesLocal, golesVisitante};
    }

    private List<Partido> obtenerPartidosPendientes(Torneo torneo) {

        List<Partido> pendientes = new ArrayList<>();

        for (Partido partido : torneo.getPartidos()) {

            if (partido.getEstado() == EstadoPartido.PROGRAMADO) {
                pendientes.add(partido);
            }
        }

        return pendientes;
    }

    private List<PosicionTabla> copiarTabla(Torneo torneo) {

        ServicioReportes servicioReportes = new ServicioReportes();

        List<PosicionTabla> tablaOriginal = servicioReportes.calcularTablaPosiciones(torneo);

        List<PosicionTabla> tablaCopia = new ArrayList<>();

        for (PosicionTabla posicion : tablaOriginal) {

            PosicionTabla copia = new PosicionTabla(
                    posicion.getClub(),
                    posicion.getPartidosJugados(),
                    posicion.getPuntos(),
                    posicion.getGolesAFavor(),
                    posicion.getGolesEnContra()
            );

            tablaCopia.add(copia);
        }

        return tablaCopia;
    }

    private void ejecutarSimulacion(Torneo torneo, List<PosicionTabla> tabla) {

        List<Partido> pendientes = obtenerPartidosPendientes(torneo);

        for (Partido partido : pendientes) {

            Club local = partido.getClubLocal();
            Club visitante = partido.getClubVisitante();

            double xGLocal =
                    servicioProyecciones.calcularXGPromedio(local, torneo);

            double xGVisitante =
                    servicioProyecciones.calcularXGPromedio(visitante, torneo);

            int[] resultado = simularPartido(xGLocal, xGVisitante);

            PosicionTabla posicionLocal = buscarPosicion(tabla, local);
            PosicionTabla posicionVisitante = buscarPosicion(tabla, visitante);

            posicionLocal.registrarResultado(resultado[0], resultado[1]);
            posicionVisitante.registrarResultado(resultado[1], resultado[0]);
        }
    }

    private PosicionTabla buscarPosicion(List<PosicionTabla> tabla, Club club) {

        for (PosicionTabla posicion : tabla) {
            if (posicion.getClub().equals(club)) {
                return posicion;
            }
        }

        throw new IllegalStateException("No se encontró el club en la tabla.");
    }



}