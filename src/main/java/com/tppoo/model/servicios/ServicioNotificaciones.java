package com.tppoo.model.servicios;

public class ServicioNotificaciones {

    public void enviarNotificacion(String mensaje, Suscriptor suscriptor) {
        if (suscriptor != null && mensaje != null && !mensaje.trim().isEmpty()) {
            suscriptor.recibirNotificacion(mensaje);
        }
    }
}