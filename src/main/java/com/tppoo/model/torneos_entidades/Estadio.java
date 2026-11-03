package com.tppoo.model.competencia

import java.time.LocalDateTime;
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

    public Estadio() {
    }

    public boolean validarDisponibilidadHoraria(LocalDateTime fechaHora) {
        return true;
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
}