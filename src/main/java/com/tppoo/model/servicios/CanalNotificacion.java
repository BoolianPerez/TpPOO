package com.tppoo.model.servicios;

import com.tppoo.model.excepciones.CanalNotificacionException;

import java.io.Serializable;

/** Abstracción de un canal de envío (OCP: un canal nuevo es una clase nueva). */
public interface CanalNotificacion extends Serializable {
    void enviar(Suscriptor destinatario, Notificacion notificacion) throws CanalNotificacionException;
}
