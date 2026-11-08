package com.tppoo.Vista;

import com.tppoo.Controlador.ControladorPersonas;
import com.tppoo.Modelo.Persona.Arbitro;
import com.tppoo.Modelo.Persona.CategoriaArbitro;
import com.tppoo.Modelo.Club.Club;
import com.tppoo.Modelo.Persona.Entrenador;
import com.tppoo.Modelo.Persona.EspecialidadEntrenador;
import com.tppoo.Modelo.Persona.Jugador;
import com.tppoo.Modelo.Persona.Persona;

import java.time.LocalDate;

public class VistaPersonas {
    private final Consola consola;
    private final ControladorPersonas controlador;

    public VistaPersonas(Consola consola, ControladorPersonas controlador) {
        this.consola = consola;
        this.controlador = controlador;
    }

    public void ejecutar() {
        int opcion;
        do {
            opcion = consola.menu("Personas", "Alta de jugador", "Alta de árbitro", "Alta de entrenador",
                    "Listar jugadores", "Listar árbitros", "Listar entrenadores",
                    "Actualizar valor de mercado de un jugador", "Activar/desactivar licencia de entrenador");
            switch (opcion) {
                case 1 -> consola.intentar(this::altaJugador);
                case 2 -> consola.intentar(this::altaArbitro);
                case 3 -> consola.intentar(this::altaEntrenador);
                case 4 -> consola.listar("Jugadores", controlador.listarJugadores(), VistaPersonas::describirJugador);
                case 5 -> consola.listar("Árbitros", controlador.listarArbitros(), VistaPersonas::describirArbitro);
                case 6 -> consola.listar("Entrenadores", controlador.listarEntrenadores(), VistaPersonas::describirEntrenador);
                case 7 -> consola.intentar(this::actualizarValor);
                case 8 -> consola.intentar(this::cambiarLicencia);
                default -> { }
            }
        } while (opcion != 0);
    }

    private void altaJugador() {
        String dni = consola.leerTexto("DNI");
        String nombre = consola.leerTexto("Nombre");
        String apellido = consola.leerTexto("Apellido");
        LocalDate nacimiento = consola.leerFecha("Fecha de nacimiento");
        String posicion = consola.leerTexto("Posición");
        int camiseta = consola.leerEntero("Número de camiseta");
        double valor = consola.leerDecimal("Valor de mercado");
        Jugador jugador = controlador.crearJugador(dni, nombre, apellido, nacimiento, posicion, camiseta, valor);
        consola.mostrar("Jugador creado: " + describirJugador(jugador));
    }

    private void altaArbitro() {
        String dni = consola.leerTexto("DNI");
        String nombre = consola.leerTexto("Nombre");
        String apellido = consola.leerTexto("Apellido");
        LocalDate nacimiento = consola.leerFecha("Fecha de nacimiento");
        String colegio = consola.leerTexto("Colegio de árbitros");
        CategoriaArbitro categoria = consola.elegirEnum("Categoría", CategoriaArbitro.class);
        if (categoria == null) {
            return;
        }
        Club hincha = null;
        if (consola.confirmar("¿Es hincha de algún club?")) {
            hincha = consola.elegir("Club del que es hincha", controlador.listarClubes(), Club::getNombre);
        }
        Arbitro arbitro = controlador.crearArbitro(dni, nombre, apellido, nacimiento, colegio, categoria, hincha);
        consola.mostrar("Árbitro creado: " + describirArbitro(arbitro));
    }

    private void altaEntrenador() {
        String dni = consola.leerTexto("DNI");
        String nombre = consola.leerTexto("Nombre");
        String apellido = consola.leerTexto("Apellido");
        LocalDate nacimiento = consola.leerFecha("Fecha de nacimiento");
        String licencia = consola.leerTexto("Número de licencia");
        EspecialidadEntrenador especialidad = consola.elegirEnum("Especialidad", EspecialidadEntrenador.class);
        if (especialidad == null) {
            return;
        }
        boolean activa = consola.confirmar("¿La licencia está activa?");
        Entrenador entrenador = controlador.crearEntrenador(dni, nombre, apellido, nacimiento, licencia, especialidad, activa);
        consola.mostrar("Entrenador creado: " + describirEntrenador(entrenador));
    }

    private void actualizarValor() {
        Jugador jugador = consola.elegir("Jugador", controlador.listarJugadores(), VistaPersonas::describirJugador);
        if (jugador != null) {
            controlador.actualizarValorMercado(jugador, consola.leerDecimal("Nuevo valor de mercado"));
            consola.mostrar("Actualizado: " + describirJugador(jugador));
        }
    }

    private void cambiarLicencia() {
        Entrenador entrenador = consola.elegir("Entrenador", controlador.listarEntrenadores(), VistaPersonas::describirEntrenador);
        if (entrenador != null) {
            controlador.actualizarLicencia(entrenador, consola.confirmar("¿Licencia activa?"));
            consola.mostrar("Actualizado: " + describirEntrenador(entrenador));
        }
    }

    // ------------------------------------------------------------------ descripciones (las reutilizan otras vistas)

    static String nombreCompleto(Persona persona) {
        return persona.getApellido() + ", " + persona.getNombre() + " (DNI " + persona.getDni() + ")";
    }

    static String describirJugador(Jugador jugador) {
        Club club = jugador.getClubActual();
        return nombreCompleto(jugador) + " - " + jugador.getPosicion() + " #" + jugador.getNumeroCamiseta()
                + " - " + (club != null ? club.getNombre() : "sin club");
    }

    static String describirArbitro(Arbitro arbitro) {
        return nombreCompleto(arbitro) + " - " + arbitro.getCategoria() + " - " + arbitro.getColegio();
    }

    static String describirEntrenador(Entrenador entrenador) {
        return nombreCompleto(entrenador) + " - " + entrenador.getEspecialidad()
                + (entrenador.validarLicencia() ? " (licencia activa)" : " (licencia inactiva)");
    }
}
