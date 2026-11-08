package com.tppoo.model.competencia;

import com.tppoo.model.torneos_entidades.Club;
import com.tppoo.model.torneos_entidades.EstadoPartido;
import com.tppoo.model.torneos_entidades.Evento;
import com.tppoo.model.torneos_entidades.Gol;
import com.tppoo.model.torneos_entidades.InscripcionClub;
import com.tppoo.model.torneos_entidades.Jugador;
import com.tppoo.model.torneos_entidades.Partido;
import com.tppoo.model.torneos_entidades.TipoGol;
import com.tppoo.model.torneos_entidades.Torneo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ServicioReportes {

    /**
     * Tabla de posiciones. Solo cuentan partidos finalizados (RN14).
     * Desempate: diferencia de gol, goles a favor y, si persiste, resultado entre los clubes empatados.
     */
    public List<PosicionTabla> calcularTablaPosiciones(Torneo torneo) {
        List<PosicionTabla> tabla = new ArrayList<>();
        for (InscripcionClub inscripcion : torneo.getInscripciones()) {
            tabla.add(new PosicionTabla(inscripcion.getClub(), 0, 0, 0, 0));
        }

        for (Partido partido : torneo.getPartidos()) {
            if (partido.getEstado() != EstadoPartido.FINALIZADO) {
                continue;
            }
            buscarPosicion(tabla, partido.getClubLocal())
                    .registrarResultado(partido.getMarcadorLocal(), partido.getMarcadorVisitante());
            buscarPosicion(tabla, partido.getClubVisitante())
                    .registrarResultado(partido.getMarcadorVisitante(), partido.getMarcadorLocal());
        }

        tabla.sort(Comparator.naturalOrder()); // orden natural de PosicionTabla

        // Los grupos empatados en los 3 criterios se resuelven con una mini-tabla entre ellos.
        List<PosicionTabla> resultado = new ArrayList<>();
        int i = 0;
        while (i < tabla.size()) {
            int j = i + 1;
            while (j < tabla.size() && tabla.get(i).compareTo(tabla.get(j)) == 0) {
                j++;
            }
            List<PosicionTabla> grupo = new ArrayList<>(tabla.subList(i, j));
            if (grupo.size() > 1) {
                desempatarEntreSi(grupo, torneo);
            }
            resultado.addAll(grupo);
            i = j;
        }
        return resultado;
    }

    private PosicionTabla buscarPosicion(List<PosicionTabla> tabla, Club club) {
        for (PosicionTabla posicion : tabla) {
            if (posicion.getClub().equals(club)) {
                return posicion;
            }
        }
        throw new IllegalStateException("No se encontró una posición para el club: " + club.getNombre());
    }

    /** Ordena un grupo de clubes empatados según los partidos jugados entre ellos (comparador personalizado). */
    private void desempatarEntreSi(List<PosicionTabla> grupo, Torneo torneo) {
        Set<Club> clubes = new HashSet<>();
        Map<Club, int[]> mini = new HashMap<>(); // [puntos, golesAFavor, golesEnContra]
        for (PosicionTabla p : grupo) {
            clubes.add(p.getClub());
            mini.put(p.getClub(), new int[3]);
        }

        for (Partido partido : torneo.getPartidos()) {
            if (partido.getEstado() != EstadoPartido.FINALIZADO
                    || !clubes.contains(partido.getClubLocal()) || !clubes.contains(partido.getClubVisitante())) {
                continue;
            }
            acumular(mini.get(partido.getClubLocal()), partido.getMarcadorLocal(), partido.getMarcadorVisitante());
            acumular(mini.get(partido.getClubVisitante()), partido.getMarcadorVisitante(), partido.getMarcadorLocal());
        }

        grupo.sort((a, b) -> {
            int[] ma = mini.get(a.getClub());
            int[] mb = mini.get(b.getClub());
            if (ma[0] != mb[0]) {
                return Integer.compare(mb[0], ma[0]);
            }
            int difA = ma[1] - ma[2];
            int difB = mb[1] - mb[2];
            if (difA != difB) {
                return Integer.compare(difB, difA);
            }
            if (ma[1] != mb[1]) {
                return Integer.compare(mb[1], ma[1]);
            }
            return String.valueOf(a.getClub().getNombre()).compareToIgnoreCase(String.valueOf(b.getClub().getNombre()));
        });
    }

    private void acumular(int[] fila, int golesAFavor, int golesEnContra) {
        fila[1] += golesAFavor;
        fila[2] += golesEnContra;
        if (golesAFavor > golesEnContra) {
            fila[0] += 3;
        } else if (golesAFavor == golesEnContra) {
            fila[0] += 1;
        }
    }

    /** Goles por jugador: cuentan a favor y de penal, no los en contra (RN15). Ignora goles anulados. */
    private Map<Jugador, Integer> contarGoles(Torneo torneo) {
        Map<Jugador, Integer> goles = new HashMap<>();
        for (Partido partido : torneo.getPartidos()) {
            if (partido.getEstado() != EstadoPartido.FINALIZADO) {
                continue;
            }
            for (Evento evento : partido.getEventos()) {
                if (evento instanceof Gol gol && !gol.estaAnulado() && gol.getTipo() != TipoGol.EN_CONTRA) {
                    goles.merge(gol.getJugadorInvolucrado(), 1, Integer::sum);
                }
            }
        }
        return goles;
    }

    /** Ranking completo de goleadores, de más a menos goles (desempata por apellido). */
    public Map<Jugador, Integer> obtenerRankingGoleadores(Torneo torneo) {
        List<Map.Entry<Jugador, Integer>> entradas = new ArrayList<>(contarGoles(torneo).entrySet());
        entradas.sort(Map.Entry.<Jugador, Integer>comparingByValue().reversed()
                .thenComparing(e -> String.valueOf(e.getKey().getApellido())));
        Map<Jugador, Integer> ranking = new LinkedHashMap<>();
        for (Map.Entry<Jugador, Integer> e : entradas) {
            ranking.put(e.getKey(), e.getValue());
        }
        return ranking;
    }

    /** Jugadores con más goles (puede haber varios empatados). */
    public List<Jugador> obtenerGoleadores(Torneo torneo) {
        Map<Jugador, Integer> ranking = obtenerRankingGoleadores(torneo);
        List<Jugador> goleadores = new ArrayList<>();
        int maximo = 0;
        for (Map.Entry<Jugador, Integer> e : ranking.entrySet()) {
            if (e.getValue() > maximo) {
                maximo = e.getValue();
                goleadores.clear();
            }
            if (e.getValue() == maximo) {
                goleadores.add(e.getKey());
            }
        }
        return goleadores;
    }

    /** Club(es) con menor promedio de goles recibidos por partido. */
    public List<Club> calcularVallaMenosVencida(Torneo torneo) {
        List<PosicionTabla> tabla = calcularTablaPosiciones(torneo);
        List<Club> mejores = new ArrayList<>();
        double menorPromedio = Double.MAX_VALUE;

        for (PosicionTabla posicion : tabla) {
            if (posicion.getPartidosJugados() == 0) {
                continue;
            }
            double promedio = (double) posicion.getGolesEnContra() / posicion.getPartidosJugados();
            if (promedio < menorPromedio) {
                menorPromedio = promedio;
                mejores.clear();
                mejores.add(posicion.getClub());
            } else if (Double.compare(promedio, menorPromedio) == 0) {
                mejores.add(posicion.getClub());
            }
        }
        return mejores;
    }
}
