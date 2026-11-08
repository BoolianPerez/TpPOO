package com.tppoo.Modelo.Partido;

import com.tppoo.Modelo.Excepcion.EventoInvalidoException;

public class Anulacion extends Evento {
    private final Evento eventoAnulado;

    public Anulacion(Evento eventoAnulado, int minuto) {
        super(eventoAnulado == null ? null : eventoAnulado.getJugadorInvolucrado(),
                eventoAnulado == null ? null : eventoAnulado.getPartido(),
                minuto);
        this.eventoAnulado = eventoAnulado;
    }

    @Override
    protected void aplicar() {
        if (eventoAnulado == null) {
            throw new EventoInvalidoException("La anulación debe referenciar un evento.");
        }
        if (eventoAnulado instanceof Anulacion) {
            throw new EventoInvalidoException("Una anulación no puede anularse.");
        }
        if (!getPartido().getEventos().contains(eventoAnulado)) {
            throw new EventoInvalidoException("El evento a anular no pertenece a este partido.");
        }
        eventoAnulado.anular();
    }

    @Override
    protected void revertir() {
        throw new EventoInvalidoException("Una anulación no puede anularse.");
    }

    @Override
    boolean exigeJugadorActivo() {
        return false;
    }

    public Evento getEventoAnulado() {
        return eventoAnulado;
    }
}
