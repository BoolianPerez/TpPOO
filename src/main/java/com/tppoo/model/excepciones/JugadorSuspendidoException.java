package com.tppoo.model.excepciones;

/** RN12: un jugador suspendido no puede incluirse en la nómina ni ser titular. */
public class JugadorSuspendidoException extends ReglaNegocioException {
    public JugadorSuspendidoException(String mensaje) {
        super(mensaje);
    }
}
