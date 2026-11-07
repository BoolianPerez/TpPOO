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
        if (estaProcesado()) {
            return;
        }
        Jugador jugador = getJugadorInvolucrado();
        Partido partido = getPartido();

        if (jugador == null || partido == null) {
            throw new IllegalStateException(
                    "La tarjeta debe tener un jugador y un partido asociados."
            );
        }

        Torneo torneo = partido.getTorneo();

        if (torneo == null) {
            throw new IllegalStateException(
                    "El partido no está asociado a un torneo."
            );
        }

        InscripcionClub inscripcion = torneo.buscarInscripcion(jugador);

        if (inscripcion == null) {
            throw new IllegalArgumentException(
                    "El jugador no pertenece a ningún club del torneo."
            );
        }

        if (color == null) {
            throw new IllegalStateException(
                    "La tarjeta debe tener un color."
            );
        }

        if (color == ColorTarjeta.AMARILLA) {

            int amarillasAcumuladas = inscripcion.registrarAmarilla(jugador);

            if (amarillasAcumuladas == 5) {
                torneo.agregarSancion(
                        new Sancion(jugador, torneo, 1, "Acumulación de cinco amarillas", partido)
                );
            }

            long amarillasEnPartido = partido.getEventos().stream()
                    .filter(e -> e instanceof Tarjeta)
                    .map(e -> (Tarjeta) e)
                    .filter(t -> t.getJugadorInvolucrado() == jugador)
                    .filter(t -> t.getColor() == ColorTarjeta.AMARILLA)
                    .count();

            if (amarillasEnPartido >= 2) {
                partido.expulsarJugador(jugador);
            }

        } else if (color == ColorTarjeta.ROJA) {

            partido.expulsarJugador(jugador);

            torneo.agregarSancion(
                    new Sancion(jugador, torneo, 1, "Tarjeta roja directa", partido)
            );
        }
        marcarProcesado();
    }

    public ColorTarjeta getColor() {
        return color;
    }

    public void setColor(ColorTarjeta color) {
        this.color = color;
    }
}