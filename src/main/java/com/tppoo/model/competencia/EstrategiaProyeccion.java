package com.tppoo.model.competencia;

import com.tppoo.model.torneos_entidades.Club;

public interface EstrategiaProyeccion {
    double calcularProbabilidad(Club club);
}