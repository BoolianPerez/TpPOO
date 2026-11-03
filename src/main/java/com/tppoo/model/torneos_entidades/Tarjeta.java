package com.tppoo.model.competencia

public class Tarjeta extends Evento {
    private ColorTarjeta color;

    @Override
    public void procesar() {
        // TODO: aplicar logica de tarjeta.
    }

    public ColorTarjeta getColor() {
        return color;
    }

    public void setColor(ColorTarjeta color) {
        this.color = color;
    }
}