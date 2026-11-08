package com.tppoo.Modelo.Notificacion;

import com.tppoo.Modelo.Excepcion.CanalNotificacionException;

public class CanalSms implements CanalNotificacion {
    @Override
    public void enviar(Suscriptor destinatario, Notificacion notificacion) throws CanalNotificacionException {
        if (destinatario == null || notificacion == null) {
            throw new CanalNotificacionException("SMS: faltan destinatario o notificación.");
        }
        if (destinatario.getTelefono() == null || destinatario.getTelefono().isBlank()) {
            throw new CanalNotificacionException("SMS: " + destinatario.getNombre() + " no tiene teléfono registrado.");
        }
        System.out.println("[SMS -> " + destinatario.getTelefono() + "] " + notificacion);
    }
}
