package com.tppoo.model.torneos_entidades;

public class Gol extends Evento {
    private TipoGol tipo;
    private Club clubAnotador;

    public Gol() {
        super();
    }

    public Gol(Jugador jugadorInvolucrado, Partido partido, int minuto, TipoGol tipo, Club clubAnotador) {
        super(jugadorInvolucrado, partido, minuto);
        this.tipo = tipo;
        this.clubAnotador = clubAnotador;
    }


    @Override
    public void procesar() {
        if (estaProcesado()) {
            return;
        }

        Partido partido = getPartido();
        Jugador jugador = getJugadorInvolucrado();

        if (partido == null || clubAnotador == null || tipo == null) {
            throw new IllegalStateException(
                    "El gol debe tener partido, club anotador y tipo asociados."
            );
        }

        boolean esLocal = clubAnotador.equals(partido.getClubLocal());
        boolean esVisitante = clubAnotador.equals(partido.getClubVisitante());

        if (!esLocal && !esVisitante) {
            throw new IllegalArgumentException(
                    "El club anotador no participa en este partido."
            );
        }

        boolean golParaLocal =
                (tipo != TipoGol.EN_CONTRA && esLocal)
                        || (tipo == TipoGol.EN_CONTRA && esVisitante);

        if (golParaLocal) {
            partido.setMarcadorLocal(partido.getMarcadorLocal() + 1);
        } else {
            partido.setMarcadorVisitante(partido.getMarcadorVisitante() + 1);
        }

        if (jugador != null && tipo != TipoGol.EN_CONTRA) {
            jugador.setGoles(jugador.getGoles() + 1);
        }

        marcarProcesado();
    }

    public TipoGol getTipo() {
        return tipo;
    }

    public void setTipo(TipoGol tipo) {
        this.tipo = tipo;
    }

    public Club getClubAnotador() {
        return clubAnotador;
    }

    public void setClubAnotador(Club clubAnotador) {
        this.clubAnotador = clubAnotador;
    }
}