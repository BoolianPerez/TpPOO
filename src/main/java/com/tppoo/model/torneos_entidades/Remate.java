package com.tppoo.model.competencia;

public class Remate extends Evento {
    private ZonaRemate zona;

    public Remate() {
        super();
    }

    public Remate(Jugador jugadorInvolucrado, Partido partido, int minuto, ZonaRemate zona) {
        super(jugadorInvolucrado, partido, minuto);
        this.zona = zona;
    }

    @Override
    public void procesar() {
        double valorXG = calcularXG();
        if (getJugadorInvolucrado() != null) {
            getJugadorInvolucrado().acumularXG(valorXG);
        }
    }

    public double calcularXG() {
        if (zona == null) {
            return 0.0;
        }
        switch (zona) {
            case AREA_CHICA:
                return 0.40;
            case AREA_GRANDE:
                return 0.15;
            case FUERA_AREA:
                return 0.04;
            case PENAL:
                return 0.76;
            default:
                return 0.0;
        }
    }

    public ZonaRemate getZona() {
        return zona;
    }

    public void setZona(ZonaRemate zona) {
        this.zona = zona;
    }
}