package com.tppoo.Controlador;

import com.tppoo.Modelo.ModeloSistema;
import com.tppoo.Modelo.Notificacion.ServicioNotificaciones;
import com.tppoo.Modelo.Partido.Anulacion;
import com.tppoo.Modelo.Persona.Arbitro;
import com.tppoo.Modelo.Partido.Cambio;
import com.tppoo.Modelo.Partido.ColorTarjeta;
import com.tppoo.Modelo.Club.Estadio;
import com.tppoo.Modelo.Partido.Evento;
import com.tppoo.Modelo.Partido.Falta;
import com.tppoo.Modelo.Partido.Gol;
import com.tppoo.Modelo.Persona.Jugador;
import com.tppoo.Modelo.Partido.Partido;
import com.tppoo.Modelo.Partido.Remate;
import com.tppoo.Modelo.Partido.Tarjeta;
import com.tppoo.Modelo.Partido.TernaArbitral;
import com.tppoo.Modelo.Partido.TipoGol;
import com.tppoo.Modelo.Partido.ZonaRemate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ControladorPartidos {
    private final ModeloSistema modelo;
    private final ServicioNotificaciones servicioNotificaciones;
    private Partido partidoActual;

    public ControladorPartidos(ModeloSistema modelo) {
        this.modelo = modelo;
        this.servicioNotificaciones = modelo.getServicioNotificaciones();
    }

    public List<Partido> listarPartidos() {
        return modelo.getPartidos();
    }

    public List<Estadio> listarEstadios() {
        return modelo.getEstadios();
    }

    public List<Arbitro> listarArbitros() {
        return modelo.getArbitros();
    }

    public Partido getPartidoActual() {
        return partidoActual;
    }

    public void setPartidoActual(Partido partido) {
        this.partidoActual = partido;
        if (partido != null) {
            partido.quitarObservador(servicioNotificaciones);
            partido.agregarObservador(servicioNotificaciones);
        }
    }

    public void asignarEstadio(Estadio estadio) {
        exigirPartido().asignarEstadio(estadio);
    }

    public void asignarTerna(Arbitro principal, Arbitro asistente1, Arbitro asistente2) {
        exigirPartido().asignarTerna(new TernaArbitral(principal, asistente1, asistente2));
    }

    public void reprogramar(LocalDateTime nuevaFechaHora) {
        exigirPartido().reprogramar(nuevaFechaHora);
    }

    public void prepararNomina() {
        exigirPartido().prepararPartido();
    }

    public void cargarTitulares(List<Jugador> titularesLocal, List<Jugador> titularesVisitante) {
        exigirPartido().cargarTitulares(new ArrayList<>(titularesLocal), new ArrayList<>(titularesVisitante));
    }

    public void iniciarPartido() {
        exigirPartido().iniciar();
    }

    public void suspenderPartido() {
        exigirPartido().suspender();
    }

    public void finalizarPartido() {
        exigirPartido().finalizar();
    }


    public void registrarEvento(Evento evento) {
        exigirPartido().registrarEvento(evento);
    }

    public void registrarGol(Jugador jugador, int minuto, TipoGol tipo) {
        registrarEvento(new Gol(jugador, exigirPartido(), minuto, tipo));
    }

    public void registrarTarjeta(Jugador jugador, int minuto, ColorTarjeta color) {
        registrarEvento(new Tarjeta(jugador, exigirPartido(), minuto, color));
    }

    public void registrarRemate(Jugador jugador, int minuto, ZonaRemate zona) {
        registrarEvento(new Remate(jugador, exigirPartido(), minuto, zona));
    }

    public void registrarFalta(Jugador jugador, int minuto) {
        registrarEvento(new Falta(jugador, exigirPartido(), minuto));
    }

    public void registrarCambio(Jugador sale, Jugador entra, int minuto) {
        registrarEvento(new Cambio(exigirPartido(), minuto, sale, entra));
    }

    public void anularEvento(Evento evento, int minuto) {
        registrarEvento(new Anulacion(evento, minuto));
    }

    private Partido exigirPartido() {
        if (partidoActual == null) {
            throw new IllegalStateException("No hay un partido seleccionado.");
        }
        return partidoActual;
    }
}
