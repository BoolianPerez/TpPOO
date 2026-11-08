package com.tppoo.model.excepciones;

/** RN04: el plantel debe tener entre 18 y 30 jugadores y un jugador solo puede estar en un club por torneo. */
public class PlantelInvalidoException extends ReglaNegocioException {
    public PlantelInvalidoException(String mensaje) {
        super(mensaje);
    }
}
