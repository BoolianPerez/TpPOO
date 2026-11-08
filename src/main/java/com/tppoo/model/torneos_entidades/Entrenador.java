package com.tppoo.model.torneos_entidades;

import java.time.LocalDate;

public class Entrenador extends Persona {
    private String licencia;
    private EspecialidadEntrenador especialidad;
    private boolean licenciaActiva;

    public Entrenador() {
    }

    public Entrenador(String dni, String nombre, String apellido, LocalDate fechaNacimiento,
                      String licencia, EspecialidadEntrenador especialidad, boolean licenciaActiva) {
        super(dni, nombre, apellido, fechaNacimiento);
        this.licencia = licencia;
        this.especialidad = especialidad;
        this.licenciaActiva = licenciaActiva;
    }

    @Override
    public String obtenerDetalle() {
        return "Entrenador: " + super.obtenerDetalle();
    }

    public boolean validarLicencia() {
        return this.licenciaActiva && this.licencia != null && !this.licencia.trim().isEmpty();
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

    public boolean isLicenciaActiva() {
        return licenciaActiva;
    }

    public void setLicenciaActiva(boolean licenciaActiva) {
        this.licenciaActiva = licenciaActiva;
    }
}
