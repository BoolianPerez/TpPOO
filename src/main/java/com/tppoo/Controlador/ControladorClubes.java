package com.tppoo.Controlador;

import com.tppoo.Modelo.ModeloSistema;
import com.tppoo.Modelo.Club.Club;
import com.tppoo.Modelo.Club.Contrato;
import com.tppoo.Modelo.Persona.Entrenador;
import com.tppoo.Modelo.Club.Estadio;
import com.tppoo.Modelo.Persona.Jugador;
import com.tppoo.Modelo.Club.Patrocinio;
import com.tppoo.Modelo.Club.Sponsor;

import java.time.LocalDate;
import java.util.List;

public class ControladorClubes {
    private final ModeloSistema modelo;

    public ControladorClubes(ModeloSistema modelo) {
        this.modelo = modelo;
    }

    public Club crearClub(String nombre, LocalDate fundacion, String sedeSocial) {
        Club club = new Club(nombre, fundacion, sedeSocial);
        modelo.agregarClub(club);
        return club;
    }

    public void asignarEstadioLocal(Club club, Estadio estadio) {
        club.setEstadioLocal(estadio);
    }

    public void agregarEntrenador(Club club, Entrenador entrenador) {
        club.agregarEntrenador(entrenador);
    }

    public Contrato contratarJugador(Club club, Jugador jugador, LocalDate inicio, LocalDate fin) {
        return club.contratar(jugador, inicio, fin);
    }

    public void rescindirContrato(Club club, Jugador jugador, LocalDate fecha) {
        club.rescindirContrato(jugador, fecha);
    }

    public Contrato transferirJugador(Club origen, Jugador jugador, Club destino, LocalDate fecha, LocalDate finContrato) {
        return origen.transferir(jugador, destino, fecha, finContrato);
    }


    public Estadio crearEstadio(String nombre, int capacidad, String calle, int numero, String ciudad, String provincia) {
        Estadio estadio = new Estadio(nombre, capacidad, calle, numero, ciudad, provincia);
        modelo.agregarEstadio(estadio);
        return estadio;
    }

    public void agregarCondicion(Estadio estadio, String condicion) {
        estadio.agregarCondicion(condicion);
    }


    public Sponsor crearSponsor(String nombre, String marca, String logo) {
        Sponsor sponsor = new Sponsor(nombre, marca, logo);
        modelo.agregarSponsor(sponsor);
        return sponsor;
    }

    public Patrocinio patrocinarClub(Club club, Sponsor sponsor, double monto, LocalDate inicio, LocalDate fin,
                                     boolean principal) {
        Patrocinio patrocinio = new Patrocinio(monto, inicio, fin, principal, sponsor);
        club.agregarPatrocinio(patrocinio);
        return patrocinio;
    }


    public List<Club> listarClubes() {
        return modelo.getClubes();
    }

    public List<Estadio> listarEstadios() {
        return modelo.getEstadios();
    }

    public List<Sponsor> listarSponsors() {
        return modelo.getSponsors();
    }

    public List<Jugador> listarJugadores() {
        return modelo.getJugadores();
    }

    public List<Entrenador> listarEntrenadores() {
        return modelo.getEntrenadores();
    }
}
