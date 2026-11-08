package com.tppoo.model.torneos_entidades;

import java.io.Serializable;

/**
 * Observador de hechos relevantes de un partido (patrón Observer).
 * Vive en el dominio para que Partido no dependa de las notificaciones (inversión de dependencias).
 */
public interface ObservadorPartido extends Serializable {
    void onGol(Partido partido, Gol gol);

    void onTarjetaRoja(Partido partido, Tarjeta tarjeta);

    void onFinPartido(Partido partido);
}
