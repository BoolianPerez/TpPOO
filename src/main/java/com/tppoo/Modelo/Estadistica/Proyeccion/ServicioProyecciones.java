package com.tppoo.Modelo.Estadistica.Proyeccion;

import com.tppoo.Modelo.Club.Club;
import com.tppoo.Modelo.Estadistica.CalculadorXG;
import com.tppoo.Modelo.Partido.Partido;
import com.tppoo.Modelo.Torneo.Torneo;

public class ServicioProyecciones {
    private EstrategiaProyeccion estrategia;
    private final CalculadorXG calculadorXG;

    public ServicioProyecciones(EstrategiaProyeccion estrategia, CalculadorXG calculadorXG) {
        this.estrategia = estrategia;
        this.calculadorXG = calculadorXG;
    }

    public double calcularProbabilidadClasificacion(Torneo torneo, Club club) {
        return estrategia.calcularProbabilidad(torneo, club);
    }

    public double calcularXG(Partido partido, Club club) {
        return calculadorXG.calcularXG(partido, club);
    }

    public double calcularXGPromedio(Club club, Torneo torneo) {
        return calculadorXG.calcularXGPromedio(club, torneo);
    }

    public EstrategiaProyeccion getEstrategia() {
        return estrategia;
    }

    public void setEstrategia(EstrategiaProyeccion estrategia) {
        this.estrategia = estrategia;
    }
}
