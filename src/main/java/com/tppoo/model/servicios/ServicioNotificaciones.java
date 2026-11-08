package com.tppoo.model.servicios;

import com.tppoo.model.excepciones.CanalNotificacionException;
import com.tppoo.model.torneos_entidades.Evento;
import com.tppoo.model.torneos_entidades.Gol;
import com.tppoo.model.torneos_entidades.ObservadorPartido;
import com.tppoo.model.torneos_entidades.Partido;
import com.tppoo.model.torneos_entidades.Tarjeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Fabricación pura: observa los partidos y reparte los avisos a los suscriptores
 * interesados por cada uno de sus canales. Un fallo en un canal se registra y no
 * frena a los demás (RN13).
 */
public class ServicioNotificaciones implements ObservadorPartido {
    private final List<Suscriptor> suscriptores = new ArrayList<>();
    private final List<String> registroFallos = new ArrayList<>();
    private int enviosIntentados = 0;

    public void agregarSuscriptor(Suscriptor suscriptor) {
        if (suscriptor != null && !suscriptores.contains(suscriptor)) {
            suscriptores.add(suscriptor);
        }
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

    /** Envía la notificación a quienes siguen al club local o al visitante, por cada canal habilitado. */
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
