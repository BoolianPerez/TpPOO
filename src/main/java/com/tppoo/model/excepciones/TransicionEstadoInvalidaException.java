package com.tppoo.model.excepciones;

/** Cambio de estado no permitido en un torneo o partido (RN01, RN02, RN08). */
public class TransicionEstadoInvalidaException extends ReglaNegocioException {
    public TransicionEstadoInvalidaException(String mensaje) {
        super(mensaje);
    }
}
