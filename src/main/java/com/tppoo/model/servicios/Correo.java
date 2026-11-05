package com.tppoo.model.servicios;

public class Correo implements CanalNotificacion {
    @Override
    public void enviar(String mensaje) {
        if (mensaje != null && !mensaje.trim().isEmpty()) {
            System.out.println("[Correo] Enviando correo electrónico con contenido: " + mensaje);
        }
    }
}