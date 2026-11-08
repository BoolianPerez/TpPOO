package com.tppoo.Modelo.Club;

import com.tppoo.Modelo.Excepcion.EstadioNoDisponibleException;
import com.tppoo.Modelo.Partido.Partido;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Estadio implements Serializable {
    public static final long MINUTOS_MINIMOS_ENTRE_PARTIDOS = 180;

    private String nombre;
    private int capacidad;
    private String calle;
    private int numero;
    private String ciudad;
    private String provincia;
    private List<String> condicionesHabituales = new ArrayList<>();
    private final List<Partido> partidos = new ArrayList<>();

    public Estadio() {
    }

    public Estadio(String nombre, int capacidad, String calle, int numero, String ciudad, String provincia) {
        validarCapacidad(capacidad);
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.calle = calle;
        this.numero = numero;
        this.ciudad = ciudad;
        this.provincia = provincia;
    }

    private static void validarCapacidad(int capacidad) {
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad del estadio debe ser mayor a cero.");
        }
    }

    public boolean validarDisponibilidadHoraria(LocalDateTime fechaHoraNueva) {
        return validarDisponibilidadHoraria(fechaHoraNueva, null);
    }

    public boolean validarDisponibilidadHoraria(LocalDateTime fechaHoraNueva, Partido ignorar) {
        if (fechaHoraNueva == null) {
            return false;
        }
        for (Partido partido : partidos) {
            if (partido == ignorar || partido.getFechaHora() == null) {
                continue;
            }
            long diferencia = Math.abs(ChronoUnit.MINUTES.between(partido.getFechaHora(), fechaHoraNueva));
            if (diferencia < MINUTOS_MINIMOS_ENTRE_PARTIDOS) {
                return false;
            }
        }
        return true;
    }

    public void reservar(Partido partido) {
        if (partido == null || partido.getFechaHora() == null) {
            throw new IllegalArgumentException("El partido debe tener fecha y hora para reservar el estadio.");
        }
        if (!validarDisponibilidadHoraria(partido.getFechaHora(), partido)) {
            throw new EstadioNoDisponibleException(
                    "El estadio " + nombre + " no puede alojar un partido con menos de 3 horas de diferencia respecto a otro.");
        }
        if (!partidos.contains(partido)) {
            partidos.add(partido);
        }
    }

    public void liberar(Partido partido) {
        partidos.remove(partido);
    }

    public void agregarCondicion(String condicion) {
        if (condicion != null && !condicion.isBlank() && !condicionesHabituales.contains(condicion)) {
            condicionesHabituales.add(condicion);
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
        validarCapacidad(capacidad);
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

    public List<String> getCondicionesHabituales() {
        return Collections.unmodifiableList(condicionesHabituales);
    }

    public List<Partido> getPartidos() {
        return Collections.unmodifiableList(partidos);
    }
}
