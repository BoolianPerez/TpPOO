package com.tppoo.Modelo.Partido;

/** Zona desde la que se patea. Cada zona lleva su xG fijo (RN16). */
public enum ZonaRemate {
    AREA_CHICA(0.40),
    AREA_GRANDE(0.15),
    FUERA_AREA(0.04),
    PENAL(0.76);

    private final double xg;

    ZonaRemate(double xg) {
        this.xg = xg;
    }

    public double getXg() {
        return xg;
    }
}
