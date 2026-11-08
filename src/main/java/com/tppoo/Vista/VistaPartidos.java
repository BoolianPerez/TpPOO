package com.tppoo.Vista;

import com.tppoo.Controlador.ControladorPartidos;
import com.tppoo.Modelo.Persona.Arbitro;
import com.tppoo.Modelo.Partido.ColorTarjeta;
import com.tppoo.Modelo.Club.Estadio;
import com.tppoo.Modelo.Partido.Evento;
import com.tppoo.Modelo.Persona.Jugador;
import com.tppoo.Modelo.Partido.Partido;
import com.tppoo.Modelo.Partido.TipoGol;
import com.tppoo.Modelo.Partido.ZonaRemate;

import java.util.ArrayList;
import java.util.List;

public class VistaPartidos {
    private final Consola consola;
    private final ControladorPartidos controlador;

    public VistaPartidos(Consola consola, ControladorPartidos controlador) {
        this.consola = consola;
        this.controlador = controlador;
    }

    public void ejecutar() {
        int opcion;
        do {
            Partido actual = controlador.getPartidoActual();
            consola.mostrar("\nPartido seleccionado: " + (actual == null ? "ninguno" : describirPartido(actual)));
            opcion = consola.menu("Partidos",
                    "Seleccionar partido", "Listar todos los partidos",
                    "Asignar estadio", "Asignar terna arbitral", "Reprogramar",
                    "Preparar nómina (jugadores habilitados)", "Cargar los 22 titulares",
                    "Iniciar partido", "Registrar evento", "Anular evento", "Suspender partido", "Finalizar partido",
                    "Ver detalle y eventos");
            switch (opcion) {
                case 1 -> seleccionar();
                case 2 -> consola.listar("Partidos", controlador.listarPartidos(), VistaPartidos::describirPartido);
                case 3 -> consola.intentar(this::asignarEstadio);
                case 4 -> consola.intentar(this::asignarTerna);
                case 5 -> consola.intentar(() -> controlador.reprogramar(consola.leerFechaHora("Nueva fecha y hora")));
                case 6 -> consola.intentar(this::prepararNomina);
                case 7 -> consola.intentar(this::cargarTitulares);
                case 8 -> consola.intentar(this::iniciar);
                case 9 -> consola.intentar(this::registrarEvento);
                case 10 -> consola.intentar(this::anularEvento);
                case 11 -> consola.intentar(this::suspender);
                case 12 -> consola.intentar(this::finalizar);
                case 13 -> consola.intentar(this::detalle);
                default -> { }
            }
        } while (opcion != 0);
    }

    private void seleccionar() {
        Partido partido = consola.elegir("Partido", controlador.listarPartidos(), VistaPartidos::describirPartido);
        if (partido != null) {
            controlador.setPartidoActual(partido);
        }
    }

    private void asignarEstadio() {
        Estadio estadio = consola.elegir("Estadio", controlador.listarEstadios(), VistaClubes::describirEstadio);
        if (estadio != null) {
            controlador.asignarEstadio(estadio);
            consola.mostrar("Estadio asignado.");
        }
    }

    private void asignarTerna() {
        List<Arbitro> arbitros = controlador.listarArbitros();
        Arbitro principal = consola.elegir("Árbitro principal", arbitros, VistaPersonas::describirArbitro);
        Arbitro asistente1 = principal == null ? null : consola.elegir("Asistente 1", arbitros, VistaPersonas::describirArbitro);
        Arbitro asistente2 = asistente1 == null ? null : consola.elegir("Asistente 2", arbitros, VistaPersonas::describirArbitro);
        if (asistente2 != null) {
            controlador.asignarTerna(principal, asistente1, asistente2);
            consola.mostrar("Terna asignada.");
        }
    }

    private void prepararNomina() {
        controlador.prepararNomina();
        Partido partido = controlador.getPartidoActual();
        consola.mostrar("Nómina local: " + partido.getNominaLocal().size() + " jugadores. Nómina visitante: "
                + partido.getNominaVisitante().size() + " jugadores (los suspendidos quedan afuera).");
    }

    private void cargarTitulares() {
        Partido partido = exigirPartidoSeleccionado();
        List<Jugador> locales = elegirOnce("titulares de " + partido.getClubLocal().getNombre(), partido.getNominaLocal());
        List<Jugador> visitantes = locales == null ? null
                : elegirOnce("titulares de " + partido.getClubVisitante().getNombre(), partido.getNominaVisitante());
        if (visitantes != null) {
            controlador.cargarTitulares(locales, visitantes);
            consola.mostrar("Titulares cargados.");
        }
    }

    /** Pide 11 jugadores de una nómina (sin repetir). Devuelve null si se cancela. */
    private List<Jugador> elegirOnce(String descripcion, List<Jugador> nomina) {
        List<Jugador> elegidos = new ArrayList<>();
        while (elegidos.size() < 11) {
            List<Jugador> restantes = new ArrayList<>(nomina);
            restantes.removeAll(elegidos);
            Jugador jugador = consola.elegir("Elegí los " + descripcion + " (" + elegidos.size() + "/11)",
                    restantes, VistaPersonas::describirJugador);
            if (jugador == null) {
                return null;
            }
            elegidos.add(jugador);
        }
        return elegidos;
    }

    private void iniciar() {
        controlador.iniciarPartido();
        consola.mostrar("El partido comenzó.");
    }

    private void suspender() {
        if (consola.confirmar("¿Suspender el partido?")) {
            controlador.suspenderPartido();
            consola.mostrar("Partido suspendido.");
        }
    }

    private void finalizar() {
        controlador.finalizarPartido();
        Partido partido = controlador.getPartidoActual();
        consola.mostrar("Final: " + describirPartido(partido));
        if (partido.getFallosDeObservadores() > 0) {
            consola.mostrar("(hubo " + partido.getFallosDeObservadores() + " fallos al notificar; el partido no se vio afectado)");
        }
    }


    private void registrarEvento() {
        int tipo = consola.menu("Tipo de evento", "Gol", "Tarjeta", "Remate", "Falta", "Cambio");
        if (tipo == 0) {
            return;
        }
        if (tipo == 5) {
            registrarCambio();
            return;
        }
        Jugador jugador = elegirJugadorEnCancha("Jugador involucrado");
        if (jugador == null) {
            return;
        }
        int minuto = consola.leerEntero("Minuto (0-120)");
        switch (tipo) {
            case 1 -> {
                TipoGol tipoGol = consola.elegirEnum("Tipo de gol", TipoGol.class);
                if (tipoGol != null) {
                    controlador.registrarGol(jugador, minuto, tipoGol);
                }
            }
            case 2 -> {
                ColorTarjeta color = consola.elegirEnum("Color", ColorTarjeta.class);
                if (color != null) {
                    controlador.registrarTarjeta(jugador, minuto, color);
                }
            }
            case 3 -> {
                ZonaRemate zona = consola.elegirEnum("Zona del remate", ZonaRemate.class);
                if (zona != null) {
                    controlador.registrarRemate(jugador, minuto, zona);
                }
            }
            default -> controlador.registrarFalta(jugador, minuto);
        }
        consola.mostrar("Evento registrado. Marcador: " + marcador(controlador.getPartidoActual()));
    }

    private void registrarCambio() {
        Jugador sale = elegirJugadorEnCancha("Jugador que sale");
        if (sale == null) {
            return;
        }
        Partido partido = controlador.getPartidoActual();
        List<Jugador> banco = new ArrayList<>(partido.getNominaLocal());
        banco.addAll(partido.getNominaVisitante());
        banco.removeAll(partido.getJugadoresEnCancha());
        Jugador entra = consola.elegir("Jugador que entra", banco, VistaPersonas::describirJugador);
        if (entra != null) {
            controlador.registrarCambio(sale, entra, consola.leerEntero("Minuto (0-120)"));
            consola.mostrar("Cambio registrado.");
        }
    }

    private void anularEvento() {
        Partido partido = exigirPartidoSeleccionado();
        Evento evento = consola.elegir("Evento a anular", partido.getEventos(), VistaPartidos::describirEvento);
        if (evento != null) {
            controlador.anularEvento(evento, consola.leerEntero("Minuto de la anulación (0-120)"));
            consola.mostrar("Evento anulado. Marcador: " + marcador(partido));
        }
    }

    private Jugador elegirJugadorEnCancha(String titulo) {
        Partido partido = exigirPartidoSeleccionado();
        return consola.elegir(titulo, partido.getJugadoresEnCancha(), VistaPersonas::describirJugador);
    }

    private void detalle() {
        Partido partido = exigirPartidoSeleccionado();
        consola.titulo(describirPartido(partido));
        consola.mostrar("Torneo: " + partido.getTorneo().getNombre());
        consola.mostrar("Estadio: " + (partido.getEstadio() != null ? partido.getEstadio().getNombre() : "sin asignar"));
        consola.mostrar("Terna: " + (partido.getTerna() == null ? "sin asignar" : nombres(partido.getTerna().getArbitros())));
        consola.mostrar("Cambios: " + partido.getClubLocal().getNombre() + " " + partido.getCambiosRealizados(partido.getClubLocal())
                + " - " + partido.getClubVisitante().getNombre() + " " + partido.getCambiosRealizados(partido.getClubVisitante()));
        consola.listar("Expulsados", partido.getJugadoresExpulsados(), VistaPersonas::describirJugador);
        consola.listar("Eventos", partido.getEventos(), VistaPartidos::describirEvento);
    }

    private String nombres(List<Arbitro> arbitros) {
        List<String> nombres = new ArrayList<>();
        for (Arbitro arbitro : arbitros) {
            nombres.add(arbitro.getApellido());
        }
        return String.join(", ", nombres);
    }

    private Partido exigirPartidoSeleccionado() {
        Partido partido = controlador.getPartidoActual();
        if (partido == null) {
            throw new IllegalStateException("Primero seleccioná un partido.");
        }
        return partido;
    }


    static String marcador(Partido partido) {
        return partido.getClubLocal().getNombre() + " " + partido.getMarcadorLocal()
                + " - " + partido.getMarcadorVisitante() + " " + partido.getClubVisitante().getNombre();
    }

    static String describirPartido(Partido partido) {
        return partido.getFechaHora().format(Consola.FORMATO_FECHA_HORA) + " | " + marcador(partido)
                + " [" + partido.getEstado() + "]";
    }

    static String describirEvento(Evento evento) {
        Jugador jugador = evento.getJugadorInvolucrado();
        return "min " + evento.getMinuto() + " - " + evento.getClass().getSimpleName()
                + (jugador != null ? " - " + jugador.getApellido() : "")
                + (evento.estaAnulado() ? " (ANULADO)" : "");
    }
}
