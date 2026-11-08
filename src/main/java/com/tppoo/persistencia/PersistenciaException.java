package com.tppoo.persistencia;

/** Error al leer o escribir el archivo de datos. Verificada: la vista debe informar al usuario. */
public class PersistenciaException extends Exception {
    public PersistenciaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
