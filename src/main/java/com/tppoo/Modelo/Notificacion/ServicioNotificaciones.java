package com.tppoo.Modelo.Notificacion;

import com.tppoo.Modelo.Excepcion.CanalNotificacionException;
import com.tppoo.Modelo.Excepcion.ReglaNegocioException;
import com.tppoo.Modelo.Partido.Evento;
import com.tppoo.Modelo.Partido.Gol;
import com.tppoo.Modelo.Partido.ObservadorPartido;
import com.tppoo.Modelo.Partido.Partido;
import com.tppoo.Modelo.Partido.Tarjeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ServicioNotificaciones implements ObservadorPartido {
    private final List<Suscriptor> suscriptores = new ArrayList<>();
    private final List<String> registroFallos = new ArrayList<>();
    private int enviosIntentados = 0;

    public void agregarSuscriptor(Suscriptor suscriptor) {
        if (suscriptor == null) {
            throw new IllegalArgumentException("El suscriptor no puede ser null.");
        }
        for (Suscriptor existente : suscriptores) {
            if (existente == suscriptor
                    || (existente.getNombre() != null && existente.getNombre().equals(suscriptor.getNombre()))) {
                throw new ReglaNegocioException("Ya existe un suscriptor con el nombre " + suscriptor.getNombre() + ".");
            }
        }
        suscriptores.add(suscriptor);
    }

    public void quitarSuscriptor(Suscriptor suscriptor) {
        suscriptores.remove(suscriptor);
    }

    @Override
    public void onGol(Partido partido, Gol gol) {
        String detalle = gol.getJugadorInvolucrado().obtenerDetalle() + " (" + gol.getTipo() + ")";
        notificar(new Notificacion(partido, gol.getMinuto(), TipoHecho.GOL, detalle));
    }

    @Override
    public void onTarjetaRoja(Partido partido, Tarjeta tarjeta) {
        String detalle = tarjeta.getJugadorInvolucrado().obtenerDetalle() + " expulsado";
        notificar(new Notificacion(partido, tarjeta.getMinuto(), TipoHecho.TARJETA_ROJA, detalle));
    }

    @Override
    public void onFinPartido(Partido partido) {
        int minuto = 90;
        for (Evento evento : partido.getEventos()) {
            minuto = Math.max(minuto, evento.getMinuto());
        }
        String detalle = partido.getClubLocal().getNombre() + " " + partido.getMarcadorLocal()
                + " - " + partido.getMarcadorVisitante() + " " + partido.getClubVisitante().getNombre();
        notificar(new Notificacion(partido, minuto, TipoHecho.FIN_PARTIDO, detalle));
    }

    public void notificar(Notificacion notificacion) {
        Partido partido = notificacion.getPartido();
        for (Suscriptor suscriptor : new ArrayList<>(suscriptores)) {
            if (!suscriptor.sigueA(partido.getClubLocal()) && !suscriptor.sigueA(partido.getClubVisitante())) {
                continue;
            }
            for (CanalNotificacion canal : suscriptor.getCanalesHabilitados()) {
                try {
                    canal.enviar(suscriptor, notificacion);
                } catch (CanalNotificacionException | RuntimeException e) {
                    registroFallos.add(canal.getClass().getSimpleName() + " -> " + suscriptor.getNombre()
                            + ": " + e.getMessage());
                } finally {
                    enviosIntentados++;
                }
            }
        }
    }

    public List<Suscriptor> getSuscriptores() {
        return Collections.unmodifiableList(suscriptores);
    }

    public List<String> getRegistroFallos() {
        return Collections.unmodifiableList(registroFallos);
    }

    public int getEnviosIntentados() {
        return enviosIntentados;
    }
}
