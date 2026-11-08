package com.tppoo.model.competencia;

import com.tppoo.model.torneos_entidades.Club;
import com.tppoo.model.torneos_entidades.Torneo;

import com.tppoo.model.torneos_entidades.Club;
import com.tppoo.model.torneos_entidades.Torneo;

/** Método de cálculo de la probabilidad de clasificación (Strategy). El puntaje de corte lo recibe cada implementación. */
public interface EstrategiaProyeccion {
    double calcularProbabilidad(Torneo torneo, Club club);
}
