package com.tppoo.model.torneos_entidades;

import java.io.Serializable;
import java.util.Objects;

public class Sponsor implements Serializable {
    private String nombre;
    private String marca;
    private String logo;

    public Sponsor() {
    }

    public Sponsor(String nombre, String marca, String logo) {
        this.nombre = nombre;
        this.marca = marca;
        this.logo = logo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sponsor)) {
            return false;
        }
        Sponsor otro = (Sponsor) o;
        return Objects.equals(nombre, otro.nombre) && Objects.equals(marca, otro.marca);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, marca);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
