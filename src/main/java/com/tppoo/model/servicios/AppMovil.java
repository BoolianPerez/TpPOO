package com.tppoo.model.servicios;

public class AppMovil implements CanalNotificacion {
    @Override
    public void enviar(String mensaje) {
        if (mensaje != null && !mensaje.trim().isEmpty()) {
            System.out.println("[App Móvil] Enviando notificación push: " + mensaje);
        }
    }
}