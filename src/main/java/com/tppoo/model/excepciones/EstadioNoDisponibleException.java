package com.tppoo.model.excepciones;

/** RN03: el estadio no puede alojar dos partidos con menos de 3 horas de diferencia. */
public class EstadioNoDisponibleException extends ReglaNegocioException {
    public EstadioNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}
