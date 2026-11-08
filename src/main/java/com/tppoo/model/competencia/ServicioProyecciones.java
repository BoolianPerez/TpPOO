package com.tppoo.model.competencia;

import com.tppoo.model.torneos_entidades.Club;
import com.tppoo.model.torneos_entidades.Partido;
import com.tppoo.model.torneos_entidades.Torneo;

/** Fachada de proyecciones: delega la probabilidad en la estrategia elegida y el xG en el calculador. */
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
