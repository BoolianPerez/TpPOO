package com.tppoo.Modelo.Excepcion;


public class CanalNotificacionException extends Exception {
    public CanalNotificacionException(String mensaje) {
        super(mensaje);
    }

    public CanalNotificacionException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
