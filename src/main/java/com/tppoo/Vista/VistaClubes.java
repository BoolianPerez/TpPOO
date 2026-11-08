package com.tppoo.Vista;

import com.tppoo.Controlador.ControladorClubes;
import com.tppoo.Modelo.Club.Club;
import com.tppoo.Modelo.Persona.Entrenador;
import com.tppoo.Modelo.Club.Estadio;
import com.tppoo.Modelo.Persona.Jugador;
import com.tppoo.Modelo.Club.Sponsor;

import java.time.LocalDate;

public class VistaClubes {
    private final Consola consola;
    private final ControladorClubes controlador;

    public VistaClubes(Consola consola, ControladorClubes controlador) {
        this.consola = consola;
        this.controlador = controlador;
    }

    public void ejecutar() {
        int opcion;
        do {
            opcion = consola.menu("Clubes, estadios y sponsors",
                    "Alta de club", "Alta de estadio", "Alta de sponsor",
                    "Asignar estadio local a un club", "Agregar condición habitual a un estadio",
                    "Contratar jugador", "Rescindir contrato", "Transferir jugador",
                    "Asignar entrenador a un club", "Patrocinar un club",
                    "Listar clubes", "Ver detalle de un club", "Listar estadios", "Listar sponsors");
            switch (opcion) {
                case 1 -> consola.intentar(this::altaClub);
                case 2 -> consola.intentar(this::altaEstadio);
                case 3 -> consola.intentar(this::altaSponsor);
                case 4 -> consola.intentar(this::asignarEstadio);
                case 5 -> consola.intentar(this::agregarCondicion);
                case 6 -> consola.intentar(this::contratar);
                case 7 -> consola.intentar(this::rescindir);
                case 8 -> consola.intentar(this::transferir);
                case 9 -> consola.intentar(this::asignarEntrenador);
                case 10 -> consola.intentar(this::patrocinar);
                case 11 -> consola.listar("Clubes", controlador.listarClubes(), Club::getNombre);
                case 12 -> consola.intentar(this::detalleClub);
                case 13 -> consola.listar("Estadios", controlador.listarEstadios(), VistaClubes::describirEstadio);
                case 14 -> consola.listar("Sponsors", controlador.listarSponsors(), VistaClubes::describirSponsor);
                default -> { }
            }
        } while (opcion != 0);
    }

    private void altaClub() {
        Club club = controlador.crearClub(consola.leerTexto("Nombre"), consola.leerFecha("Fecha de fundación"),
                consola.leerTexto("Sede social"));
        consola.mostrar("Club creado: " + club.getNombre());
    }

    private void altaEstadio() {
        Estadio estadio = controlador.crearEstadio(consola.leerTexto("Nombre"), consola.leerEntero("Capacidad"),
                consola.leerTexto("Calle"), consola.leerEntero("Número"), consola.leerTexto("Ciudad"),
                consola.leerTexto("Provincia"));
        consola.mostrar("Estadio creado: " + describirEstadio(estadio));
    }

    private void altaSponsor() {
        Sponsor sponsor = controlador.crearSponsor(consola.leerTexto("Nombre"), consola.leerTexto("Marca"),
                consola.leerTextoOpcional("Logo (archivo o url)"));
        consola.mostrar("Sponsor creado: " + describirSponsor(sponsor));
    }

    private void asignarEstadio() {
        Club club = elegirClub();
        Estadio estadio = club == null ? null : consola.elegir("Estadio", controlador.listarEstadios(), VistaClubes::describirEstadio);
        if (estadio != null) {
            controlador.asignarEstadioLocal(club, estadio);
            consola.mostrar(club.getNombre() + " ahora juega de local en " + estadio.getNombre());
        }
    }

    private void agregarCondicion() {
        Estadio estadio = consola.elegir("Estadio", controlador.listarEstadios(), VistaClubes::describirEstadio);
        if (estadio != null) {
            controlador.agregarCondicion(estadio, consola.leerTexto("Condición (ej: césped sintético)"));
            consola.mostrar("Condición agregada.");
        }
    }

    private void contratar() {
        Club club = elegirClub();
        Jugador jugador = club == null ? null : elegirJugador();
        if (jugador != null) {
            LocalDate inicio = consola.leerFecha("Inicio del contrato");
            LocalDate fin = consola.leerFecha("Fin del contrato");
            controlador.contratarJugador(club, jugador, inicio, fin);
            consola.mostrar(jugador.getApellido() + " fue contratado por " + club.getNombre());
        }
    }

    private void rescindir() {
        Club club = elegirClub();
        Jugador jugador = club == null ? null
                : consola.elegir("Jugador del plantel", club.getJugadores(), j -> VistaPersonas.nombreCompleto(j));
        if (jugador != null) {
            controlador.rescindirContrato(club, jugador, consola.leerFecha("Fecha de rescisión"));
            consola.mostrar("Contrato rescindido.");
        }
    }

    private void transferir() {
        Club origen = elegirClub();
        Jugador jugador = origen == null ? null
                : consola.elegir("Jugador a transferir", origen.getJugadores(), j -> VistaPersonas.nombreCompleto(j));
        Club destino = jugador == null ? null : consola.elegir("Club destino", controlador.listarClubes(), Club::getNombre);
        if (destino != null) {
            LocalDate fecha = consola.leerFecha("Fecha de la transferencia");
            LocalDate fin = consola.leerFecha("Fin del nuevo contrato");
            controlador.transferirJugador(origen, jugador, destino, fecha, fin);
            consola.mostrar(jugador.getApellido() + " pasó de " + origen.getNombre() + " a " + destino.getNombre());
        }
    }

    private void asignarEntrenador() {
        Club club = elegirClub();
        Entrenador entrenador = club == null ? null
                : consola.elegir("Entrenador", controlador.listarEntrenadores(), VistaPersonas::describirEntrenador);
        if (entrenador != null) {
            controlador.agregarEntrenador(club, entrenador);
            consola.mostrar("Entrenador asignado a " + club.getNombre());
        }
    }

    private void patrocinar() {
        Club club = elegirClub();
        Sponsor sponsor = club == null ? null : consola.elegir("Sponsor", controlador.listarSponsors(), VistaClubes::describirSponsor);
        if (sponsor != null) {
            double monto = consola.leerDecimal("Monto");
            LocalDate inicio = consola.leerFecha("Inicio del patrocinio");
            LocalDate fin = consola.leerFecha("Fin del patrocinio");
            boolean principal = consola.confirmar("¿Es sponsor principal?");
            controlador.patrocinarClub(club, sponsor, monto, inicio, fin, principal);
            consola.mostrar("Patrocinio registrado.");
        }
    }

    private void detalleClub() {
        Club club = elegirClub();
        if (club == null) {
            return;
        }
        consola.titulo(club.getNombre());
        consola.mostrar("Fundado: " + club.getFechaFundacion().format(Consola.FORMATO_FECHA) + " - Sede: " + club.getSedeSocial());
        consola.mostrar("Estadio local: " + (club.getEstadioLocal() != null ? club.getEstadioLocal().getNombre() : "sin asignar"));
        consola.mostrar("Sponsor principal: " + (club.obtenerSponsorPrincipal() != null
                ? club.obtenerSponsorPrincipal().getNombre() : "ninguno vigente"));
        consola.listar("Plantel", club.getJugadores(), VistaPersonas::describirJugador);
        consola.listar("Cuerpo técnico", club.getEntrenadores(), VistaPersonas::describirEntrenador);
    }

    private Club elegirClub() {
        return consola.elegir("Club", controlador.listarClubes(), Club::getNombre);
    }

    private Jugador elegirJugador() {
        return consola.elegir("Jugador", controlador.listarJugadores(), VistaPersonas::describirJugador);
    }

    static String describirEstadio(Estadio estadio) {
        return estadio.getNombre() + " - " + estadio.getCiudad() + ", " + estadio.getProvincia()
                + " - capacidad " + estadio.getCapacidad();
    }

    static String describirSponsor(Sponsor sponsor) {
        return sponsor.getNombre() + " (" + sponsor.getMarca() + ")";
    }
}
