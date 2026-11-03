package com.tppoo.model.personas

import java.time.LocalDate;

public class Entrenador extends Persona {
    private String licencia;
    private EspecialidadEntrenador especialidad;

    public Entrenador() {
    }

    public Entrenador(String dni, String nombre, String apellido, LocalDate fechaNacimiento, String licencia, EspecialidadEntrenador especialidad) {
        super(dni, nombre, apellido, fechaNacimiento);
        this.licencia = licencia;
        this.especialidad = especialidad;
    }

    @Override
    public String obtenerDetalle() {
        return "Entrenador: " + super.obtenerDetalle();
    }

    public String getLicencia() {
        return licencia;
    }

    public void setLicencia(String licencia) {
        this.licencia = licencia;
    }

    public EspecialidadEntrenador getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(EspecialidadEntrenador especialidad) {
        this.especialidad = especialidad;
    }
}