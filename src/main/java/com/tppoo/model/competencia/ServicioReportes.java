package com.tppoo.model.competencia;

import com.tppoo.model.torneos_entidades.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServicioReportes {
    public List<PosicionTabla> calcularTablaPosiciones(Torneo torneo) {
        List<PosicionTabla> tabla = new ArrayList<>();

        for (InscripcionClub inscripcion : torneo.getInscripciones()) {

            PosicionTabla posicion = new PosicionTabla(inscripcion.getClub(), 0, 0, 0, 0);

            tabla.add(posicion);
        }

        for (Partido partido : torneo.getPartidos()) {

            if (partido.getEstado() != EstadoPartido.FINALIZADO) {
                continue;
            }

            PosicionTabla posicionLocal = buscarPosicion(tabla, partido.getClubLocal());

            PosicionTabla posicionVisitante = buscarPosicion(tabla, partido.getClubVisitante());

            posicionLocal.registrarResultado(partido.getMarcadorLocal(), partido.getMarcadorVisitante());

            posicionVisitante.registrarResultado(partido.getMarcadorVisitante(), partido.getMarcadorLocal());

        }
        tabla.sort((p1, p2) -> compararPosiciones(p1, p2, torneo));

        return tabla;
    }

    private PosicionTabla buscarPosicion(List<PosicionTabla> tabla, Club club) {

        for (PosicionTabla posicion : tabla) {

            if (posicion.getClub().equals(club)) {
                return posicion;
            }
        }

        throw new IllegalStateException(
                "No se encontró una posición para el club: " + club.getNombre()
        );
    }

    private int compararPosiciones(PosicionTabla p1, PosicionTabla p2, Torneo torneo){
        if (p1.getPuntos() != p2.getPuntos()) {
            return Integer.compare(p2.getPuntos(), p1.getPuntos());
        }

        if (p1.calcularDiferenciaGoles() != p2.calcularDiferenciaGoles()) {
            return Integer.compare(p2.calcularDiferenciaGoles(), p1.calcularDiferenciaGoles());
        }

        if (p1.getGolesAFavor() != p2.getGolesAFavor()) {
            return Integer.compare(p2.getGolesAFavor(), p1.getGolesAFavor());
        }

        return desempatarPorEnfrentamientosDirectos(p1.getClub(), p2.getClub(), torneo);
    }

    private int desempatarPorEnfrentamientosDirectos(Club club1, Club club2, Torneo torneo){
        int golesClub1 = 0;
        int golesClub2 = 0;
        int amarillasClub1 = 0;
        int amarillasClub2 = 0;

        for (Partido partido : torneo.getPartidos()) {

            if (partido.getEstado() != EstadoPartido.FINALIZADO) {
                continue;
            }

            if ((partido.getClubLocal().equals(club1) && partido.getClubVisitante().equals(club2)) ||
                    (partido.getClubLocal().equals(club2) && partido.getClubVisitante().equals(club1))) {

                if (partido.getClubLocal().equals(club1)) {
                    golesClub1 += partido.getMarcadorLocal();
                    golesClub2 += partido.getMarcadorVisitante();
                } else {
                    golesClub1 += partido.getMarcadorVisitante();
                    golesClub2 += partido.getMarcadorLocal();
                }

                amarillasClub1 += contarAmarillas(partido, club1, torneo);
                amarillasClub2 += contarAmarillas(partido, club2, torneo);
            }
        }

        if (golesClub1 != golesClub2) {
            return Integer.compare(golesClub2, golesClub1);
        }

        return Integer.compare(amarillasClub1, amarillasClub2);
    }
    private int contarAmarillas(Partido partido, Club club, Torneo torneo){
        int cantidad = 0;

        for (Evento evento : partido.getEventos()) {

            if (evento instanceof Tarjeta tarjeta &&
                    tarjeta.getColor() == ColorTarjeta.AMARILLA) {

                for (InscripcionClub inscripcion : torneo.getInscripciones()) {

                    if (inscripcion.getClub().equals(club) &&
                            inscripcion.tieneJugador(evento.getJugadorInvolucrado())) {

                        cantidad++;
                        break;
                    }
                }
            }
        }

        return cantidad;
    }

    public List<Jugador> obtenerGoleadores(Torneo torneo) {
        Map<Jugador, Integer> golesPorJugador = new HashMap<>();

        for (Partido partido : torneo.getPartidos()) {

            if (partido.getEstado() != EstadoPartido.FINALIZADO) {
                continue;
            }

            for (Evento evento : partido.getEventos()) {

                if (evento instanceof Gol gol &&
                        gol.getTipo() != TipoGol.EN_CONTRA) {

                    Jugador jugador = gol.getJugadorInvolucrado();

                    if (jugador != null) {
                        golesPorJugador.put(jugador, golesPorJugador.getOrDefault(jugador, 0) + 1);
                    }
                }
            }
        }

        List<Jugador> goleadores = new ArrayList<>();
        int maxGoles = 0;

        for (Map.Entry<Jugador, Integer> entrada : golesPorJugador.entrySet()) {

            int goles = entrada.getValue();

            if (goles > maxGoles) {
                maxGoles = goles;
                goleadores.clear();
                goleadores.add(entrada.getKey());

            } else if (goles == maxGoles) {
                goleadores.add(entrada.getKey());
            }
        }

        return goleadores;
    }

    public List<Club> calcularVallaMenosVencida(Torneo torneo) {
        List<PosicionTabla> tabla = calcularTablaPosiciones(torneo);
        List<Club> mejoresClubes = new ArrayList<>();

        double menorPromedio = Double.MAX_VALUE;

        for (PosicionTabla posicion : tabla) {

            if (posicion.getPartidosJugados() == 0) {
                continue;
            }

            double promedio = (double) posicion.getGolesEnContra()
                    / posicion.getPartidosJugados();

            if (promedio < menorPromedio) {
                menorPromedio = promedio;
                mejoresClubes.clear();
                mejoresClubes.add(posicion.getClub());

            } else if (Double.compare(promedio, menorPromedio) == 0) {
                mejoresClubes.add(posicion.getClub());
            }
        }

        return mejoresClubes;
    }
}