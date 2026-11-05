package com.tppoo.model.servicios;

public class SMS implements CanalNotificacion {
    @Override
    public void enviar(String mensaje) {
        if (mensaje != null && !mensaje.trim().isEmpty()) {
            System.out.println("[SMS] Enviando mensaje de texto: " + mensaje);
        }
    }
}