package com.tppoo.model.torneos_entidades;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Estadio {
    private String nombre;
    private int capacidad;
    private String calle;
    private int numero;
    private String ciudad;
    private String provincia;
    private List<String> ubicacionesDisponibles = new ArrayList<>();

    private List<Partido> partidos = new ArrayList<>();

    public Estadio() {
    }

    public Estadio(String nombre, int capacidad, String calle, int numero, String ciudad, String provincia) {
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.calle = calle;
        this.numero = numero;
        this.ciudad = ciudad;
        this.provincia = provincia;
    }

    public boolean validarDisponibilidadHoraria(LocalDateTime fechaHoraNueva) {
        if (fechaHoraNueva == null) {
            return false;
        }

        for (Partido partido : partidos) {
            long diferenciaMinutos = Math.abs(ChronoUnit.MINUTES.between(partido.getFechaHora(), fechaHoraNueva));

            if (diferenciaMinutos < 180) {
                return false;
            }
        }
        return true;
    }

    public void agregarPartido(Partido partido) {
        if (validarDisponibilidadHoraria(partido.getFechaHora())) {
            this.partidos.add(partido);
        } else {
            throw new IllegalStateException("RN03: El estadio no puede alojar un partido con menos de 3 horas de diferencia respecto a otro.");
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public List<String> getUbicacionesDisponibles() {
        return ubicacionesDisponibles;
    }

    public void setUbicacionesDisponibles(List<String> ubicacionesDisponibles) {
        this.ubicacionesDisponibles = ubicacionesDisponibles;
    }

    public List<Partido> getPartidos() {
        return partidos;
    }

    public void setPartidos(List<Partido> partidos) {
        this.partidos = partidos;
    }
}