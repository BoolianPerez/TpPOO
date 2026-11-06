package com.tppoo.model.competencia;

import com.tppoo.model.torneos_entidades.Club;

public class ServicioProyecciones {
    private EstrategiaProyeccion estrategia;

    public ServicioProyecciones(EstrategiaProyeccion estrategia) {
        this.estrategia = estrategia;
    }

    public double calcularProbabilidadClasificacion(Club club) {
        return estrategia.calcularProbabilidad(club);
    }

    public double calcularXG() {
        return 0.0;
    }

    public EstrategiaProyeccion getEstrategia() {
        return estrategia;
    }

    public void setEstrategia(EstrategiaProyeccion estrategia) {
        this.estrategia = estrategia;
    }
}