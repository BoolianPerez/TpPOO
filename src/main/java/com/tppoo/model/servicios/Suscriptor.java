package com.tppoo.model.servicios;

import com.tppoo.model.torneos_entidades.Club;

import java.util.ArrayList;
import java.util.List;

public class Suscriptor {
    private String nombre;
    private List<Club> clubesQueSigue = new ArrayList<>();
    private List<CanalNotificacion> canalesHabituales = new ArrayList<>();

    public Suscriptor() {
    }

    public Suscriptor(String nombre) {
        this.nombre = nombre;
    }

    public void recibirNotificacion(String mensaje) {
        if (mensaje != null && !mensaje.isEmpty()) {
            for (CanalNotificacion canal : canalesHabituales) {
                canal.enviar(mensaje);
            }
        }
    }

    public void seguirClub(Club club) {
        if (club != null && !clubesQueSigue.contains(club)) {
            clubesQueSigue.add(club);
        }
    }

    public void dejarDeSeguirClub(Club club) {
        clubesQueSigue.remove(club);
    }


    public void agregarCanal(CanalNotificacion canal) {
        if (canal != null && !canalesHabituales.contains(canal)) {
            canalesHabituales.add(canal);
        }
    }

    public void removerCanal(CanalNotificacion canal) {
        canalesHabituales.remove(canal);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Club> getClubesQueSigue() {
        return clubesQueSigue;
    }

    public void setClubesQueSigue(List<Club> clubesQueSigue) {
        this.clubesQueSigue = clubesQueSigue;
    }

    public List<CanalNotificacion> getCanalesHabituales() {
        return canalesHabituales;
    }

    public void setCanalesHabituales(List<CanalNotificacion> canalesHabituales) {
        this.canalesHabituales = canalesHabituales;
    }
}