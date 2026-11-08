package com.tppoo.model.servicios;

import com.tppoo.model.excepciones.CanalNotificacionException;

public class CanalAppMovil implements CanalNotificacion {
    @Override
    public void enviar(Suscriptor destinatario, Notificacion notificacion) throws CanalNotificacionException {
        if (destinatario == null || notificacion == null) {
            throw new CanalNotificacionException("App móvil: faltan destinatario o notificación.");
        }
        if (destinatario.getTokenApp() == null || destinatario.getTokenApp().isBlank()) {
            throw new CanalNotificacionException("App móvil: " + destinatario.getNombre() + " no tiene token registrado.");
        }
        System.out.println("[App Móvil -> " + destinatario.getNombre() + "] push: " + notificacion);
    }
}
