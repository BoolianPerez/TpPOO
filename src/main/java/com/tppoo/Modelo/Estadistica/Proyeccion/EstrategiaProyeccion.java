package com.tppoo.Modelo.Estadistica.Proyeccion;

import com.tppoo.Modelo.Club.Club;
import com.tppoo.Modelo.Torneo.Torneo;

public interface EstrategiaProyeccion {
    double calcularProbabilidad(Torneo torneo, Club club);
}
