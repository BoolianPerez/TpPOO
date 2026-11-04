package com.tppoo.model.competencia;

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
        if (getPartido() != null && clubAnotador != null) {
            boolean esLocal = clubAnotador.equals(getPartido().getClubLocal());

            if (tipo != TipoGol.EN_CONTRA) {
                if (esLocal) {
                    getPartido().setMarcadorLocal(getPartido().getMarcadorLocal() + 1);
                } else {
                    getPartido().setMarcadorVisitante(getPartido().getMarcadorVisitante() + 1);
                }
            } else {
                if (esLocal) {
                    getPartido().setMarcadorVisitante(getPartido().getMarcadorVisitante() + 1);
                } else {
                    getPartido().setMarcadorLocal(getPartido().getMarcadorLocal() + 1);
                }
            }

            if (getJugadorInvolucrado() != null && tipo != TipoGol.EN_CONTRA) {
                getJugadorInvolucrado().incrementarGoles();
            }
        }
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