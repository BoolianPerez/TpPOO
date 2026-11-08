package com.tppoo.model.excepciones;

/**
 * Falla técnica al enviar por un canal de notificación (RN13).
 * Es verificada: quien envía está obligado a decidir qué hacer (el servicio la registra y sigue).
 */
public class CanalNotificacionException extends Exception {
    public CanalNotificacionException(String mensaje) {
        super(mensaje);
    }

    public CanalNotificacionException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
