package com.tppoo.model.servicios;

import com.tppoo.model.excepciones.CanalNotificacionException;

public class CanalCorreo implements CanalNotificacion {
    @Override
    public void enviar(Suscriptor destinatario, Notificacion notificacion) throws CanalNotificacionException {
        if (destinatario == null || notificacion == null) {
            throw new CanalNotificacionException("Correo: faltan destinatario o notificación.");
        }
        if (destinatario.getEmail() == null || destinatario.getEmail().isBlank()) {
            throw new CanalNotificacionException("Correo: " + destinatario.getNombre() + " no tiene email registrado.");
        }
        System.out.println("[Correo -> " + destinatario.getEmail() + "] " + notificacion);
    }
}
