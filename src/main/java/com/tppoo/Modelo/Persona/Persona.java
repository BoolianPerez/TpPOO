package com.tppoo.Modelo.Persona;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

public abstract class Persona implements Serializable {
    private String dni;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;

    public Persona() {
    }

    public Persona(String dni, String nombre, String apellido, LocalDate fechaNacimiento) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
    }

    public int calcularEdad() {
        if (fechaNacimiento == null) {
            return 0;
        }
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    public String obtenerDetalle() {
        return nombre + " " + apellido;
    }

    public boolean mismoDni(Persona otra) {
        return otra != null && dni != null && dni.equals(otra.dni);
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
}
