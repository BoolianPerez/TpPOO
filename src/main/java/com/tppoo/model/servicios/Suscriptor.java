package com.tppoo.model.notificaciones

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
        // TODO: procesar notificacion del suscriptor.
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