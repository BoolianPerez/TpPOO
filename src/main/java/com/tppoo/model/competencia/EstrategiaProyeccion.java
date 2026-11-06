package com.tppoo.model.competencia;

import com.tppoo.model.torneos_entidades.Club;
import com.tppoo.model.torneos_entidades.Torneo;

public interface EstrategiaProyeccion {
    double calcularProbabilidad(Torneo torneo, Club club);

    static final int PUNTAJE_CORTE = 45;
}