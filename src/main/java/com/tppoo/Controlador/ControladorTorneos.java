package com.tppoo.Controlador;

import com.tppoo.Modelo.ModeloSistema;
import com.tppoo.Modelo.Persona.Arbitro;
import com.tppoo.Modelo.Club.Club;
import com.tppoo.Modelo.Club.Estadio;
import com.tppoo.Modelo.Torneo.InscripcionClub;
import com.tppoo.Modelo.Persona.Jugador;
import com.tppoo.Modelo.Partido.Partido;
import com.tppoo.Modelo.Club.Patrocinio;
import com.tppoo.Modelo.Torneo.Sancion;
import com.tppoo.Modelo.Club.Sponsor;
import com.tppoo.Modelo.Partido.TernaArbitral;
import com.tppoo.Modelo.Torneo.Torneo;
import com.tppoo.Modelo.Excepcion.ReglaNegocioException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ControladorTorneos {
    private final ModeloSistema modelo;

    public ControladorTorneos(ModeloSistema modelo) {
        this.modelo = modelo;
    }

    public Torneo crearTorneo(String nombre, LocalDate inicio, LocalDate fin, boolean internacional) {
        Torneo torneo = new Torneo(nombre, inicio, fin, internacional);
        modelo.agregarTorneo(torneo);
        return torneo;
    }

    public void inscribirClub(Torneo torneo, Club club) {
        torneo.inscribirClub(club);
    }

    public void inscribirJugador(Torneo torneo, Club club, Jugador jugador) {
        inscripcionDe(torneo, club).agregarJugador(jugador);
    }

    public void quitarJugador(Torneo torneo, Club club, Jugador jugador) {
        inscripcionDe(torneo, club).quitarJugador(jugador);
    }

    public boolean plantelValido(Torneo torneo, Club club) {
        return inscripcionDe(torneo, club).plantelValido();
    }

    public void avanzarEstado(Torneo torneo) {
        torneo.avanzarEstado();
    }

    public void generarFixture(Torneo torneo) {
        torneo.generarFixture();
    }

    public TernaArbitral crearTerna(Arbitro principal, Arbitro asistente1, Arbitro asistente2) {
        return new TernaArbitral(principal, asistente1, asistente2);
    }

    public Partido programarPartido(Torneo torneo, Club local, Club visitante, LocalDateTime fechaHora,
                                    Estadio estadio, TernaArbitral terna) {
        return torneo.programarPartido(local, visitante, fechaHora, estadio, terna);
    }

    public Patrocinio patrocinarTorneo(Torneo torneo, Sponsor sponsor, double monto, LocalDate inicio, LocalDate fin) {
        Patrocinio patrocinio = new Patrocinio(monto, inicio, fin, false, sponsor);
        torneo.agregarPatrocinio(patrocinio);
        return patrocinio;
    }

    public void sancionar(Torneo torneo, Jugador jugador, int partidos, String motivo) {
        torneo.agregarSancion(new Sancion(jugador, torneo, partidos, motivo, null));
    }

    private InscripcionClub inscripcionDe(Torneo torneo, Club club) {
        InscripcionClub inscripcion = torneo.buscarInscripcion(club);
        if (inscripcion == null) {
            throw new ReglaNegocioException("El club no está inscripto en el torneo.");
        }
        return inscripcion;
    }


    public List<Torneo> listarTorneos() {
        return modelo.getTorneos();
    }

    public List<Club> listarClubes() {
        return modelo.getClubes();
    }

    public List<Estadio> listarEstadios() {
        return modelo.getEstadios();
    }

    public List<Arbitro> listarArbitros() {
        return modelo.getArbitros();
    }

    public List<Sponsor> listarSponsors() {
        return modelo.getSponsors();
    }

    public List<Jugador> jugadoresDisponibles(Club club) {
        return club.getJugadores();
    }

    public List<Partido> listarPartidos(Torneo torneo) {
        return torneo.getPartidos();
    }

    public List<Sancion> listarSanciones(Torneo torneo) {
        return torneo.getSanciones();
    }
}
