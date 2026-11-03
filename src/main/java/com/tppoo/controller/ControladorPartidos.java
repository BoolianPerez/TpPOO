package com.tppoo.controller;

import com.tppoo.model.Evento;
import com.tppoo.model.Partido;

public class ControladorPartidos {
    private Partido partidoActual;

    public void iniciarPartido() {
        if (partidoActual != null) {
            partidoActual.iniciar();
        }
    }

    public void registrarEvento(Evento evento) {
        if (partidoActual != null) {
            partidoActual.registrarEvento(evento);
        }
    }

    public void finalizarPartido() {
        if (partidoActual != null) {
            partidoActual.finalizar();
        }
    }

    public Partido getPartidoActual() {
        return partidoActual;
    }

    public void setPartidoActual(Partido partidoActual) {
        this.partidoActual = partidoActual;
    }
}