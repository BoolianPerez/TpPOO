package com.tppoo.Modelo.Excepcion;

public class TransicionEstadoInvalidaException extends ReglaNegocioException {
    public TransicionEstadoInvalidaException(String mensaje) {
        super(mensaje);
    }
}
