package com.tppoo.Modelo.Excepcion;

public class LimiteCambiosExcedidoException extends ReglaNegocioException {
    public LimiteCambiosExcedidoException(String mensaje) {
        super(mensaje);
    }
}
