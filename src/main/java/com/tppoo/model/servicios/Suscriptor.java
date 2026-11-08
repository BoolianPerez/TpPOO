package com.tppoo.model.servicios;

import com.tppoo.model.torneos_entidades.Club;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Quien recibe avisos: elige qué clubes sigue y por qué canales. No sabe enviar nada. */
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
        if (club != null && !clubesQueSigue.contains(club)) {
            clubesQueSigue.add(club);
        }
    }

    public void dejarDeSeguirClub(Club club) {
        clubesQueSigue.remove(club);
    }

    public boolean sigueA(Club club) {
        return club != null && clubesQueSigue.contains(club);
    }

    /** Habilita un canal. No admite dos canales del mismo tipo (CanalSms, CanalCorreo, ...). */
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
