package com.tppoo.Modelo.Excepcion;

public class EventoInvalidoException extends ReglaNegocioException {
    public EventoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
