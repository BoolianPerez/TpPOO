package com.tppoo.Modelo.Partido;

import java.io.Serializable;

public interface ObservadorPartido extends Serializable {
    void onGol(Partido partido, Gol gol);

    void onTarjetaRoja(Partido partido, Tarjeta tarjeta);

    void onFinPartido(Partido partido);
}
