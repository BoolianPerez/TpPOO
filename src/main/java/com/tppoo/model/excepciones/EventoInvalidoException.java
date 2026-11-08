package com.tppoo.model.excepciones;

/** Evento de partido que viola RN08, RN09, RN10 o RN11. */
public class EventoInvalidoException extends ReglaNegocioException {
    public EventoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
