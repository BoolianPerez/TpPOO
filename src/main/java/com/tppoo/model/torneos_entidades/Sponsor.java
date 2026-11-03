package com.tppoo.model.competencia

public class Sponsor {
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