package com.tppoo.Vista;

import com.tppoo.Controlador.ControladorReportes;
import com.tppoo.Controlador.ControladorReportes.Estrategia;
import com.tppoo.Modelo.Estadistica.PosicionTabla;
import com.tppoo.Modelo.Club.Club;
import com.tppoo.Modelo.Persona.Jugador;
import com.tppoo.Modelo.Torneo.Torneo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class VistaReportes {
    private final Consola consola;
    private final ControladorReportes controlador;

    public VistaReportes(Consola consola, ControladorReportes controlador) {
        this.consola = consola;
        this.controlador = controlador;
    }

    public void ejecutar() {
        int opcion;
        do {
            opcion = consola.menu("Reportes y proyecciones", "Tabla de posiciones", "Ranking de goleadores",
                    "Valla menos vencida", "xG promedio de un club", "Probabilidad de clasificación de un club");
            switch (opcion) {
                case 1 -> consola.intentar(this::tabla);
                case 2 -> consola.intentar(this::goleadores);
                case 3 -> consola.intentar(this::valla);
                case 4 -> consola.intentar(this::xg);
                case 5 -> consola.intentar(this::probabilidad);
                default -> { }
            }
        } while (opcion != 0);
    }

    private void tabla() {
        Torneo torneo = elegirTorneo();
        if (torneo == null) {
            return;
        }
        consola.titulo("Posiciones - " + torneo.getNombre());
        consola.mostrar(String.format("%-3s %-22s %3s %4s %4s %4s %4s", "#", "Club", "PJ", "Pts", "GF", "GC", "DG"));
        int puesto = 1;
        for (PosicionTabla fila : controlador.tablaPosiciones(torneo)) {
            consola.mostrar(String.format("%-3d %-22s %3d %4d %4d %4d %4d", puesto++, fila.getClub().getNombre(),
                    fila.getPartidosJugados(), fila.getPuntos(), fila.getGolesAFavor(), fila.getGolesEnContra(),
                    fila.calcularDiferenciaGoles()));
        }
    }

    private void goleadores() {
        Torneo torneo = elegirTorneo();
        if (torneo != null) {
            List<String> filas = new ArrayList<>();
            for (Map.Entry<Jugador, Integer> entrada : controlador.rankingGoleadores(torneo).entrySet()) {
                filas.add(entrada.getValue() + " gol(es) - " + VistaPersonas.describirJugador(entrada.getKey()));
            }
            consola.listar("Goleadores - " + torneo.getNombre(), filas, fila -> fila);
        }
    }

    private void valla() {
        Torneo torneo = elegirTorneo();
        if (torneo != null) {
            consola.listar("Valla menos vencida - " + torneo.getNombre(), controlador.vallaMenosVencida(torneo), Club::getNombre);
        }
    }

    private void xg() {
        Torneo torneo = elegirTorneo();
        Club club = torneo == null ? null : elegirClub(torneo);
        if (club != null) {
            consola.mostrar(String.format(Locale.ROOT, "xG promedio de %s: %.2f", club.getNombre(),
                    controlador.xgPromedio(torneo, club)));
        }
    }

    private void probabilidad() {
        Torneo torneo = elegirTorneo();
        Club club = torneo == null ? null : elegirClub(torneo);
        Estrategia estrategia = club == null ? null : consola.elegirEnum("Estrategia de proyección", Estrategia.class);
        if (estrategia != null) {
            int corte = consola.leerEntero("Puntaje de corte para clasificar");
            double probabilidad = controlador.probabilidadClasificacion(torneo, club, estrategia, corte);
            consola.mostrar(String.format(Locale.ROOT, "Probabilidad de que %s llegue a %d puntos: %.1f%%",
                    club.getNombre(), corte, probabilidad * 100));
        }
    }

    private Torneo elegirTorneo() {
        return consola.elegir("Torneo", controlador.listarTorneos(), VistaTorneos::describirTorneo);
    }

    private Club elegirClub(Torneo torneo) {
        return consola.elegir("Club", torneo.getClubes(), Club::getNombre);
    }
}
