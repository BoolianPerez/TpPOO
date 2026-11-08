package com.tppoo.Controlador;

import com.tppoo.Modelo.Estadistica.Proyeccion.EstrategiaProyeccion;
import com.tppoo.Modelo.Estadistica.Proyeccion.ProyeccionPorPuntos;
import com.tppoo.Modelo.Estadistica.Proyeccion.ProyeccionPorSimulacion;
import com.tppoo.Modelo.Estadistica.Proyeccion.ServicioProyecciones;
import com.tppoo.Modelo.Estadistica.ServicioReportes;
import com.tppoo.Modelo.ModeloSistema;
import com.tppoo.Modelo.Estadistica.CalculadorXG;
import com.tppoo.Modelo.Estadistica.PosicionTabla;
import com.tppoo.Modelo.Club.Club;
import com.tppoo.Modelo.Persona.Jugador;
import com.tppoo.Modelo.Torneo.Torneo;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class ControladorReportes {
    public enum Estrategia {
        POR_PUNTOS, POR_SIMULACION
    }

    private final ModeloSistema modelo;
    private final ServicioReportes reportes = new ServicioReportes();
    private final CalculadorXG calculadorXG = new CalculadorXG();
    private final Random random = new Random();

    public ControladorReportes(ModeloSistema modelo) {
        this.modelo = modelo;
    }

    public List<Torneo> listarTorneos() {
        return modelo.getTorneos();
    }

    public List<PosicionTabla> tablaPosiciones(Torneo torneo) {
        return reportes.calcularTablaPosiciones(torneo);
    }

    public Map<Jugador, Integer> rankingGoleadores(Torneo torneo) {
        return reportes.obtenerRankingGoleadores(torneo);
    }

    public List<Club> vallaMenosVencida(Torneo torneo) {
        return reportes.calcularVallaMenosVencida(torneo);
    }

    public double xgPromedio(Torneo torneo, Club club) {
        return calculadorXG.calcularXGPromedio(club, torneo);
    }

    public double probabilidadClasificacion(Torneo torneo, Club club, Estrategia estrategia, int puntajeCorte) {
        ServicioProyecciones servicio = new ServicioProyecciones(crearEstrategia(estrategia, puntajeCorte), calculadorXG);
        return servicio.calcularProbabilidadClasificacion(torneo, club);
    }

    private EstrategiaProyeccion crearEstrategia(Estrategia estrategia, int puntajeCorte) {
        if (estrategia == Estrategia.POR_SIMULACION) {
            return new ProyeccionPorSimulacion(reportes, calculadorXG, random, puntajeCorte);
        }
        return new ProyeccionPorPuntos(reportes, puntajeCorte);
    }
}
