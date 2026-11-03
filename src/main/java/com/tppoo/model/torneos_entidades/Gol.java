package com.tppoo.model.competencia

public class Gol extends Evento {
    private TipoGol tipo;

    @Override
    public void procesar() {
        // TODO: aplicar logica de gol.
    }

    public TipoGol getTipo() {
        return tipo;
    }

    public void setTipo(TipoGol tipo) {
        this.tipo = tipo;
    }
}