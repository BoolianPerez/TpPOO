package com.tppoo.Vista;

import com.tppoo.Controlador.ControladorTorneos;
import com.tppoo.Modelo.Persona.Arbitro;
import com.tppoo.Modelo.Club.Club;
import com.tppoo.Modelo.Club.Estadio;
import com.tppoo.Modelo.Persona.Jugador;
import com.tppoo.Modelo.Partido.Partido;
import com.tppoo.Modelo.Torneo.Sancion;
import com.tppoo.Modelo.Club.Sponsor;
import com.tppoo.Modelo.Partido.TernaArbitral;
import com.tppoo.Modelo.Torneo.Torneo;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class VistaTorneos {
    private final Consola consola;
    private final ControladorTorneos controlador;

    public VistaTorneos(Consola consola, ControladorTorneos controlador) {
        this.consola = consola;
        this.controlador = controlador;
    }

    public void ejecutar() {
        int opcion;
        do {
            opcion = consola.menu("Torneos",
                    "Alta de torneo", "Inscribir club", "Inscribir jugador en un club inscripto",
                    "Quitar jugador de una inscripción", "Avanzar estado del torneo", "Generar fixture",
                    "Programar partido fuera del fixture", "Patrocinar torneo", "Sancionar jugador",
                    "Listar torneos", "Ver detalle de un torneo", "Ver partidos de un torneo", "Ver sanciones de un torneo");
            switch (opcion) {
                case 1 -> consola.intentar(this::altaTorneo);
                case 2 -> consola.intentar(this::inscribirClub);
                case 3 -> consola.intentar(this::inscribirJugador);
                case 4 -> consola.intentar(this::quitarJugador);
                case 5 -> consola.intentar(this::avanzarEstado);
                case 6 -> consola.intentar(this::generarFixture);
                case 7 -> consola.intentar(this::programarPartido);
                case 8 -> consola.intentar(this::patrocinar);
                case 9 -> consola.intentar(this::sancionar);
                case 10 -> consola.listar("Torneos", controlador.listarTorneos(), VistaTorneos::describirTorneo);
                case 11 -> consola.intentar(this::detalle);
                case 12 -> consola.intentar(this::verPartidos);
                case 13 -> consola.intentar(this::verSanciones);
                default -> { }
            }
        } while (opcion != 0);
    }

    private void altaTorneo() {
        String nombre = consola.leerTexto("Nombre del torneo");
        LocalDate inicio = consola.leerFecha("Fecha de inicio");
        LocalDate fin = consola.leerFecha("Fecha de fin");
        boolean internacional = consola.confirmar("¿Es internacional?");
        Torneo torneo = controlador.crearTorneo(nombre, inicio, fin, internacional);
        consola.mostrar("Torneo creado: " + describirTorneo(torneo));
    }

    private void inscribirClub() {
        Torneo torneo = elegirTorneo();
        Club club = torneo == null ? null : consola.elegir("Club a inscribir", controlador.listarClubes(), Club::getNombre);
        if (club != null) {
            controlador.inscribirClub(torneo, club);
            consola.mostrar(club.getNombre() + " inscripto en " + torneo.getNombre());
        }
    }

    private void inscribirJugador() {
        Torneo torneo = elegirTorneo();
        Club club = torneo == null ? null : elegirClubInscripto(torneo);
        Jugador jugador = club == null ? null
                : consola.elegir("Jugador del club", controlador.jugadoresDisponibles(club), VistaPersonas::describirJugador);
        if (jugador != null) {
            controlador.inscribirJugador(torneo, club, jugador);
            consola.mostrar("Jugador inscripto. Plantel " + (controlador.plantelValido(torneo, club) ? "válido." : "todavía incompleto."));
        }
    }

    private void quitarJugador() {
        Torneo torneo = elegirTorneo();
        Club club = torneo == null ? null : elegirClubInscripto(torneo);
        Jugador jugador = club == null ? null
                : consola.elegir("Jugador inscripto", torneo.buscarInscripcion(club).getJugadores(), VistaPersonas::describirJugador);
        if (jugador != null) {
            controlador.quitarJugador(torneo, club, jugador);
            consola.mostrar("Jugador quitado de la inscripción.");
        }
    }

    private void avanzarEstado() {
        Torneo torneo = elegirTorneo();
        if (torneo != null) {
            controlador.avanzarEstado(torneo);
            consola.mostrar("Estado actual: " + torneo.getEstado());
        }
    }

    private void generarFixture() {
        Torneo torneo = elegirTorneo();
        if (torneo != null) {
            controlador.generarFixture(torneo);
            consola.mostrar("Fixture generado: " + torneo.getPartidos().size() + " partidos.");
        }
    }

    private void programarPartido() {
        Torneo torneo = elegirTorneo();
        if (torneo == null) {
            return;
        }
        Club local = consola.elegir("Club local", torneo.getClubes(), Club::getNombre);
        Club visitante = local == null ? null : consola.elegir("Club visitante", torneo.getClubes(), Club::getNombre);
        Estadio estadio = visitante == null ? null
                : consola.elegir("Estadio", controlador.listarEstadios(), VistaClubes::describirEstadio);
        if (estadio == null) {
            return;
        }
        LocalDateTime fechaHora = consola.leerFechaHora("Fecha y hora del partido");
        TernaArbitral terna = elegirTerna();
        if (terna != null) {
            Partido partido = controlador.programarPartido(torneo, local, visitante, fechaHora, estadio, terna);
            consola.mostrar("Partido programado: " + VistaPartidos.describirPartido(partido));
        }
    }

    private void patrocinar() {
        Torneo torneo = elegirTorneo();
        Sponsor sponsor = torneo == null ? null
                : consola.elegir("Sponsor", controlador.listarSponsors(), VistaClubes::describirSponsor);
        if (sponsor != null) {
            double monto = consola.leerDecimal("Monto");
            LocalDate inicio = consola.leerFecha("Inicio del patrocinio");
            LocalDate fin = consola.leerFecha("Fin del patrocinio");
            controlador.patrocinarTorneo(torneo, sponsor, monto, inicio, fin);
            consola.mostrar("Patrocinio registrado.");
        }
    }

    private void sancionar() {
        Torneo torneo = elegirTorneo();
        Club club = torneo == null ? null : elegirClubInscripto(torneo);
        Jugador jugador = club == null ? null
                : consola.elegir("Jugador", torneo.buscarInscripcion(club).getJugadores(), VistaPersonas::describirJugador);
        if (jugador != null) {
            int partidos = consola.leerEntero("Cantidad de partidos de suspensión");
            controlador.sancionar(torneo, jugador, partidos, consola.leerTexto("Motivo"));
            consola.mostrar("Sanción registrada.");
        }
    }

    private void detalle() {
        Torneo torneo = elegirTorneo();
        if (torneo == null) {
            return;
        }
        consola.titulo(torneo.getNombre());
        consola.mostrar(describirTorneo(torneo));
        consola.listar("Clubes inscriptos", torneo.getInscripciones(),
                i -> i.getClub().getNombre() + " - " + i.getJugadores().size() + " jugadores"
                        + (i.plantelValido() ? "" : " (plantel incompleto)"));
        consola.listar("Patrocinios", torneo.getPatrocinios(),
                p -> p.getSponsor().getNombre() + " - $" + p.getMonto());
    }

    private void verPartidos() {
        Torneo torneo = elegirTorneo();
        if (torneo != null) {
            consola.listar("Partidos de " + torneo.getNombre(), controlador.listarPartidos(torneo), VistaPartidos::describirPartido);
        }
    }

    private void verSanciones() {
        Torneo torneo = elegirTorneo();
        if (torneo != null) {
            consola.listar("Sanciones", controlador.listarSanciones(torneo), VistaTorneos::describirSancion);
        }
    }

    private TernaArbitral elegirTerna() {
        Arbitro principal = consola.elegir("Árbitro principal", controlador.listarArbitros(), VistaPersonas::describirArbitro);
        Arbitro asistente1 = principal == null ? null
                : consola.elegir("Asistente 1", controlador.listarArbitros(), VistaPersonas::describirArbitro);
        Arbitro asistente2 = asistente1 == null ? null
                : consola.elegir("Asistente 2", controlador.listarArbitros(), VistaPersonas::describirArbitro);
        return asistente2 == null ? null : controlador.crearTerna(principal, asistente1, asistente2);
    }

    private Torneo elegirTorneo() {
        return consola.elegir("Torneo", controlador.listarTorneos(), VistaTorneos::describirTorneo);
    }

    private Club elegirClubInscripto(Torneo torneo) {
        return consola.elegir("Club inscripto", torneo.getClubes(), Club::getNombre);
    }

    static String describirTorneo(Torneo torneo) {
        return torneo.getNombre() + " [" + torneo.getEstado() + "] "
                + torneo.getFechaInicio().format(Consola.FORMATO_FECHA) + " a " + torneo.getFechaFin().format(Consola.FORMATO_FECHA)
                + (torneo.isInternacional() ? " (internacional)" : "") + " - " + torneo.getClubes().size() + " clubes";
    }

    static String describirSancion(Sancion sancion) {
        return VistaPersonas.nombreCompleto(sancion.getJugador()) + " - " + sancion.getPartidosPendientes()
                + " partido(s) pendientes - " + sancion.getMotivo();
    }
}
