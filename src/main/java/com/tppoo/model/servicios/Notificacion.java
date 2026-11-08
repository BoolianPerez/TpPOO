package com.tppoo.model.servicios;

import com.tppoo.model.torneos_entidades.Partido;

import java.io.Serializable;

/** Aviso que indica el partido, el minuto y el hecho (pedido por el enunciado). */
public class Notificacion implements Serializable {
    private final Partido partido;
    private final int minuto;
    private final TipoHecho tipoHecho;
    private final String detalle;

    public Notificacion(Partido partido, int minuto, TipoHecho tipoHecho, String detalle) {
        if (partido == null || tipoHecho == null) {
            throw new IllegalArgumentException("La notificación requiere partido y tipo de hecho.");
        }
        this.partido = partido;
        this.minuto = minuto;
        this.tipoHecho = tipoHecho;
        this.detalle = detalle == null ? "" : detalle;
    }

    public Partido getPartido() {
        return partido;
    }

    public int getMinuto() {
        return minuto;
    }

    public TipoHecho getTipoHecho() {
        return tipoHecho;
    }

    public String getDetalle() {
        return detalle;
    }

    @Override
    public String toString() {
        String texto = "[" + partido.getClubLocal().getNombre() + " vs " + partido.getClubVisitante().getNombre()
                + "] min " + minuto + " - " + tipoHecho;
        return detalle.isEmpty() ? texto : texto + ": " + detalle;
    }
}
