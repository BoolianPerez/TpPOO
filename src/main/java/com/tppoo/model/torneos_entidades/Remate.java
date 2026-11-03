package com.tppoo.model.competencia

public class Remate extends Evento {
    private ZonaRemate zona;

    @Override
    public void procesar() {
        // TODO: aplicar logica de remate.
    }

    public double calcularXG() {
        return 0.0;
    }

    public ZonaRemate getZona() {
        return zona;
    }

    public void setZona(ZonaRemate zona) {
        this.zona = zona;
    }
}