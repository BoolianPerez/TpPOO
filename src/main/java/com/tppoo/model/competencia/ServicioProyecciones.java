package com.tppoo.model.competencia

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