package com.tppoo.model.excepciones;

/**
 * Base de todas las violaciones de reglas de negocio del sistema.
 * Es no verificada (RuntimeException) para no contaminar todas las firmas del modelo;
 * la capa de controlador/vista debe capturarla y mostrar el mensaje al usuario.
 */
public class ReglaNegocioException extends RuntimeException {
    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
