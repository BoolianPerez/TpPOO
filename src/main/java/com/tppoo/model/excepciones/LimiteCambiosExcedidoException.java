package com.tppoo.model.excepciones;

/** Un club no puede realizar más de 5 cambios por partido. */
public class LimiteCambiosExcedidoException extends ReglaNegocioException {
    public LimiteCambiosExcedidoException(String mensaje) {
        super(mensaje);
    }
}
