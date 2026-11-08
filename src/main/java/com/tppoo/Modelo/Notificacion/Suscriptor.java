package com.tppoo.Modelo.Notificacion;

import com.tppoo.Modelo.Club.Club;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Suscriptor implements Serializable {
    private String nombre;
    private String email;
    private String telefono;
    private String tokenApp;
    private final List<Club> clubesQueSigue = new ArrayList<>();
    private final List<CanalNotificacion> canalesHabilitados = new ArrayList<>();

    public Suscriptor(String nombre) {
        this.nombre = nombre;
    }

    public Suscriptor(String nombre, String email, String telefono, String tokenApp) {
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.tokenApp = tokenApp;
    }

    public void seguirClub(Club club) {
        if (club != null && !sigueA(club)) {
            clubesQueSigue.add(club);
        }
    }

    public void dejarDeSeguirClub(Club club) {
        clubesQueSigue.removeIf(c -> c == club || c.mismoNombre(club));
    }

    public boolean sigueA(Club club) {
        if (club == null) {
            return false;
        }
        for (Club seguido : clubesQueSigue) {
            if (seguido == club || seguido.mismoNombre(club)) {
                return true;
            }
        }
        return false;
    }

    public void agregarCanal(CanalNotificacion canal) {
        if (canal == null) {
            return;
        }
        for (CanalNotificacion existente : canalesHabilitados) {
            if (existente.getClass() == canal.getClass()) {
                return;
            }
        }
        canalesHabilitados.add(canal);
    }

    public void removerCanal(CanalNotificacion canal) {
        if (canal != null) {
            canalesHabilitados.removeIf(c -> c.getClass() == canal.getClass());
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTokenApp() {
        return tokenApp;
    }

    public void setTokenApp(String tokenApp) {
        this.tokenApp = tokenApp;
    }

    public List<Club> getClubesQueSigue() {
        return Collections.unmodifiableList(clubesQueSigue);
    }

    public List<CanalNotificacion> getCanalesHabilitados() {
        return Collections.unmodifiableList(canalesHabilitados);
    }
}
