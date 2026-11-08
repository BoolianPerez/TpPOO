package com.tppoo.Modelo.Partido;

import com.tppoo.Modelo.Excepcion.EventoInvalidoException;
import com.tppoo.Modelo.Persona.Jugador;
import com.tppoo.Modelo.Torneo.InscripcionClub;
import com.tppoo.Modelo.Torneo.Sancion;
import com.tppoo.Modelo.Torneo.Torneo;

public class Tarjeta extends Evento {
    private final ColorTarjeta color;
    private boolean provocoExpulsion = false;
    private Sancion sancionPorAcumulacion;
    private Sancion sancionPorExpulsion;

    public Tarjeta(Jugador jugadorInvolucrado, Partido partido, int minuto, ColorTarjeta color) {
        super(jugadorInvolucrado, partido, minuto);
        this.color = color;
    }

    @Override
    protected void aplicar() {
        Jugador jugador = getJugadorInvolucrado();
        Partido partido = getPartido();

        if (jugador == null || partido == null) {
            throw new EventoInvalidoException("La tarjeta debe tener un jugador y un partido asociados.");
        }
        if (color == null) {
            throw new EventoInvalidoException("La tarjeta debe tener un color.");
        }

        Torneo torneo = partido.getTorneo();
        InscripcionClub inscripcion = torneo.buscarInscripcion(jugador);

        if (inscripcion == null) {
            throw new EventoInvalidoException("El jugador no pertenece a ningún club del torneo.");
        }

        jugador.registrarTarjeta();

        if (color == ColorTarjeta.AMARILLA) {
            int acumuladas = inscripcion.registrarAmarilla(jugador);

            if (acumuladas % 5 == 0) {
                sancionPorAcumulacion = new Sancion(jugador, torneo, 1, "Acumulación de cinco amarillas", partido);
                torneo.agregarSancion(sancionPorAcumulacion);
            }

            // Se cuentan las amarillas ya registradas (este evento todavía no está en la lista del partido).
            long previas = partido.getEventos().stream()
                    .filter(e -> e instanceof Tarjeta)
                    .map(e -> (Tarjeta) e)
                    .filter(t -> !t.estaAnulado())
                    .filter(t -> t.getColor() == ColorTarjeta.AMARILLA)
                    .filter(t -> jugador.equals(t.getJugadorInvolucrado()))
                    .count();

            if (previas >= 1) {
                expulsar(partido, torneo, jugador, "Doble amarilla");
            }
        } else {
            expulsar(partido, torneo, jugador, "Tarjeta roja directa");
        }
    }

    private void expulsar(Partido partido, Torneo torneo, Jugador jugador, String motivo) {
        partido.expulsarJugador(jugador);
        provocoExpulsion = true;
        sancionPorExpulsion = new Sancion(jugador, torneo, 1, motivo, partido);
        torneo.agregarSancion(sancionPorExpulsion);
    }

    @Override
    protected void revertir() {
        Jugador jugador = getJugadorInvolucrado();
        Partido partido = getPartido();
        Torneo torneo = partido.getTorneo();

        jugador.quitarTarjeta();

        if (color == ColorTarjeta.AMARILLA) {
            torneo.buscarInscripcion(jugador).deshacerAmarilla(jugador);
        }
        if (sancionPorAcumulacion != null) {
            torneo.quitarSancion(sancionPorAcumulacion);
        }
        if (sancionPorExpulsion != null) {
            torneo.quitarSancion(sancionPorExpulsion);
        }
        if (provocoExpulsion) {
            partido.readmitirJugador(jugador);
        }
    }

    @Override
    void notificarA(ObservadorPartido observador) {
        if (provocoExpulsion) {
            observador.onTarjetaRoja(getPartido(), this);
        }
    }

    public boolean provocoExpulsion() {
        return provocoExpulsion;
    }

    public ColorTarjeta getColor() {
        return color;
    }
}
