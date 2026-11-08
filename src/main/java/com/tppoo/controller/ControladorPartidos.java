package com.tppoo.controller;

import com.tppoo.model.servicios.ServicioNotificaciones;
import com.tppoo.model.torneos_entidades.Evento;
import com.tppoo.model.torneos_entidades.Partido;

/** GRASP Controller: recibe las operaciones sobre un partido y coordina dominio y notificaciones. */
public class ControladorPartidos {
    private final ServicioNotificaciones servicioNotificaciones;
    private Partido partidoActual;

    public ControladorPartidos(ServicioNotificaciones servicioNotificaciones) {
        this.servicioNotificaciones = servicioNotificaciones;
    }

    public void iniciarPartido() {
        exigirPartido().iniciar();
    }

    public void registrarEvento(Evento evento) {
        exigirPartido().registrarEvento(evento);
    }

    public void finalizarPartido() {
        exigirPartido().finalizar();
    }

    private Partido exigirPartido() {
        if (partidoActual == null) {
            throw new IllegalStateException("No hay un partido seleccionado.");
        }
        return partidoActual;
    }

    public Partido getPartidoActual() {
        return partidoActual;
    }

    /** Al seleccionar un partido se lo conecta con el servicio de notificaciones (Observer). */
    public void setPartidoActual(Partido partidoActual) {
        this.partidoActual = partidoActual;
        if (partidoActual != null) {
            partidoActual.agregarObservador(servicioNotificaciones);
        }
    }
}
