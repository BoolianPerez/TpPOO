package com.tppoo.Controlador;

import com.tppoo.Modelo.ModeloSistema;
import com.tppoo.Modelo.Persona.Arbitro;
import com.tppoo.Modelo.Persona.CategoriaArbitro;
import com.tppoo.Modelo.Club.Club;
import com.tppoo.Modelo.Persona.Entrenador;
import com.tppoo.Modelo.Persona.EspecialidadEntrenador;
import com.tppoo.Modelo.Persona.Jugador;

import java.time.LocalDate;
import java.util.List;

public class ControladorPersonas {
    private final ModeloSistema modelo;

    public ControladorPersonas(ModeloSistema modelo) {
        this.modelo = modelo;
    }

    public Jugador crearJugador(String dni, String nombre, String apellido, LocalDate nacimiento,
                                String posicion, int numeroCamiseta, double valorMercado) {
        Jugador jugador = new Jugador(dni, nombre, apellido, nacimiento, posicion, numeroCamiseta, valorMercado);
        modelo.agregarJugador(jugador);
        return jugador;
    }

    public Arbitro crearArbitro(String dni, String nombre, String apellido, LocalDate nacimiento,
                                String colegio, CategoriaArbitro categoria, Club clubHincha) {
        Arbitro arbitro = new Arbitro(dni, nombre, apellido, nacimiento, colegio, categoria);
        arbitro.setClubHincha(clubHincha);
        modelo.agregarArbitro(arbitro);
        return arbitro;
    }

    public Entrenador crearEntrenador(String dni, String nombre, String apellido, LocalDate nacimiento,
                                      String licencia, EspecialidadEntrenador especialidad, boolean licenciaActiva) {
        Entrenador entrenador = new Entrenador(dni, nombre, apellido, nacimiento, licencia, especialidad, licenciaActiva);
        modelo.agregarEntrenador(entrenador);
        return entrenador;
    }

    public void actualizarValorMercado(Jugador jugador, double valor) {
        jugador.setValorMercado(valor);
    }

    public void actualizarLicencia(Entrenador entrenador, boolean activa) {
        entrenador.setLicenciaActiva(activa);
    }

    public List<Jugador> listarJugadores() {
        return modelo.getJugadores();
    }

    public List<Arbitro> listarArbitros() {
        return modelo.getArbitros();
    }

    public List<Entrenador> listarEntrenadores() {
        return modelo.getEntrenadores();
    }

    public List<Club> listarClubes() {
        return modelo.getClubes();
    }
}
