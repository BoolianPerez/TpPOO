package com.tppoo.Modelo.Notificacion;

import com.tppoo.Modelo.Excepcion.CanalNotificacionException;

import java.io.Serializable;


public interface CanalNotificacion extends Serializable {
    void enviar(Suscriptor destinatario, Notificacion notificacion) throws CanalNotificacionException;
}
