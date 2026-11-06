package com.tppoo.model.torneos_entidades;

public class Tarjeta extends Evento {
    private ColorTarjeta color;

    public Tarjeta() {
        super();
    }

    public Tarjeta(Jugador jugadorInvolucrado, Partido partido, int minuto, ColorTarjeta color) {
        super(jugadorInvolucrado, partido, minuto);
        this.color = color;
    }

    @Override
    public void procesar() {
        if (getJugadorInvolucrado() != null) {
            Jugador jugador = getJugadorInvolucrado();
            jugador.setTarjetas(this);

            if (color == ColorTarjeta.AMARILLA) {
                long amarillasEnPartido = getPartido().getEventos().stream()
                        .filter(e -> e instanceof Tarjeta && e.getJugadorInvolucrado() == jugador)
                        .map(e -> (Tarjeta) e)
                        .filter(t -> t.getColor() == ColorTarjeta.AMARILLA)
                        .count();

                if (amarillasEnPartido >= 2) {
                    jugador.marcarExpulsado();
                }
            } else if (color == ColorTarjeta.ROJA) {
                jugador.marcarExpulsado();
            }
        }
    }

    public ColorTarjeta getColor() {
        return color;
    }

    public void setColor(ColorTarjeta color) {
        this.color = color;
    }
}