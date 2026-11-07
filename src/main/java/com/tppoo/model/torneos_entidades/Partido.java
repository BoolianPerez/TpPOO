package com.tppoo.model.torneos_entidades;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Partido {
    private int marcadorVisitante;
    private Club clubVisitante;
    private int marcadorLocal;
    private Club clubLocal;
    private LocalDateTime fechaHora;
    private EstadoPartido estado;
    private Arbitro arbitroPrincipal;
    private Arbitro asistente1;
    private Arbitro asistente2;
    private Estadio estadio;
    private Torneo torneo;
    private List<Evento> eventos = new ArrayList<>();
    private List<Jugador> jugadoresExpulsados = new ArrayList<>();
    private List<Jugador> nominaLocal = new ArrayList<>();
    private List<Jugador> nominaVisitante = new ArrayList<>();
    private List<Jugador> jugadoresEnCancha = new ArrayList<>();
    private List<Jugador> jugadoresQueSalieron = new ArrayList<>();

    public Partido() {
        this.marcadorLocal = 0;
        this.marcadorVisitante = 0;
        this.estado = EstadoPartido.PROGRAMADO;
    }

    public Partido(Club clubLocal, Club clubVisitante, LocalDate fecha, Torneo torneo) {
        this.marcadorLocal = 0;
        this.marcadorVisitante = 0;
        this.estado = EstadoPartido.PROGRAMADO;
        this.clubLocal = clubLocal;
        this.clubVisitante = clubVisitante;
        this.torneo = torneo;
        if (fecha != null) {
            this.fechaHora = fecha.atStartOfDay();
        }

    }

    public void prepararPartido() {
        if (estado != EstadoPartido.PROGRAMADO) {
            throw new IllegalStateException(
                    "El partido ya fue iniciado."
            );
        }

        cargarNominaLocal();
        cargarNominaVisitante();
    }

    public void iniciar() {
        if (estado != EstadoPartido.PROGRAMADO) {
            throw new IllegalStateException("El partido no está programado.");
        }

        if (jugadoresEnCancha.size() != 22) {
            throw new IllegalStateException("Deben cargarse los titulares de ambos equipos.");
        }

        estado = EstadoPartido.EN_JUEGO;
    }

    public void registrarEvento(Evento evento) {
        if (this.estado != EstadoPartido.EN_JUEGO) {
            throw new IllegalStateException(
                    "RN08: Los eventos únicamente pueden registrarse cuando el partido está EN_JUEGO."
            );
        }

        if (evento == null) {
            throw new IllegalArgumentException(
                    "El evento no puede ser null."
            );
        }

        if (evento.getMinuto() < 0 || evento.getMinuto() > 120) {
            throw new IllegalArgumentException(
                    "RN08: El minuto del evento debe estar entre 0 y 120."
            );
        }

        if (evento.getPartido() != this) {
            throw new IllegalArgumentException(
                    "El evento no pertenece a este partido."
            );
        }

        Jugador jugador = evento.getJugadorInvolucrado();

        if (jugador != null && jugadorFueExpulsado(jugador)) {
            throw new IllegalStateException(
                    "El jugador fue expulsado y no puede seguir participando."
            );
        }

        if (this.eventos.contains(evento)) {
            throw new IllegalArgumentException(
                    "El evento ya fue registrado en este partido."
            );
        }

        this.eventos.add(evento);
        evento.procesar();
    }

    public void finalizar() {
        if (this.estado == EstadoPartido.EN_JUEGO) {
            this.estado = EstadoPartido.FINALIZADO;

            if (torneo != null) {
                torneo.cumplirSancionesDelPartido(this);
            }

        } else {
            throw new IllegalStateException(
                    "Solo se puede finalizar un partido que esté EN_JUEGO."
            );
        }
    }

    public void expulsarJugador(Jugador jugador) {
        if (estado != EstadoPartido.EN_JUEGO) {
            throw new IllegalStateException(
                    "Solo se puede expulsar a un jugador durante un partido en juego."
            );
        }

        if (jugador == null) {
            throw new IllegalArgumentException(
                    "El jugador no puede ser null."
            );
        }

        if (!jugadoresExpulsados.contains(jugador)) {
            jugadoresExpulsados.add(jugador);
        }
    }

    public void agregarJugadorNominaLocal(Jugador jugador) {
        validarJugadorNomina(jugador, clubLocal, nominaLocal);
    }

    public void agregarJugadorNominaVisitante(Jugador jugador) {
        validarJugadorNomina(jugador, clubVisitante, nominaVisitante);
    }

    private void validarJugadorNomina(
            Jugador jugador, Club club, List<Jugador> nomina) {

        if (estado != EstadoPartido.PROGRAMADO) {
            throw new IllegalStateException(
                    "No se puede modificar la nómina de un partido iniciado."
            );
        }

        if (jugador == null) {
            throw new IllegalArgumentException("El jugador no puede ser null.");
        }

        if (!torneo.buscarInscripcion(jugador).getClub().equals(club)) {
            throw new IllegalArgumentException(
                    "El jugador no pertenece a este equipo."
            );
        }

        if (torneo.jugadorEstaSuspendido(jugador)) {
            throw new IllegalStateException(
                    "El jugador está suspendido."
            );
        }

        if (nomina.contains(jugador)) {
            throw new IllegalArgumentException(
                    "El jugador ya está incluido en la nómina."
            );
        }

        nomina.add(jugador);
    }

    public void cargarNominaLocal() {
        InscripcionClub inscripcion = torneo.buscarInscripcion(clubLocal);

        if (inscripcion == null) {
            throw new IllegalStateException(
                    "El club local no está inscripto en el torneo."
            );
        }

        for (Jugador jugador : inscripcion.getJugadores()) {
            if (!torneo.jugadorEstaSuspendido(jugador)) {
                agregarJugadorNominaLocal(jugador);
            }
        }
    }

    public void cargarNominaVisitante() {
        InscripcionClub inscripcion = torneo.buscarInscripcion(clubVisitante);

        if (inscripcion == null) {
            throw new IllegalStateException(
                    "El club visitante no está inscripto en el torneo."
            );
        }

        for (Jugador jugador : inscripcion.getJugadores()) {
            if (!torneo.jugadorEstaSuspendido(jugador)) {
                agregarJugadorNominaVisitante(jugador);
            }
        }
    }

    public void registrarCambio(Jugador jugadorSale, Jugador jugadorEntra) {
        if (estado != EstadoPartido.EN_JUEGO) {
            throw new IllegalStateException("El partido no está en juego.");
        }

        if (jugadorSale == null || jugadorEntra == null) {
            throw new IllegalArgumentException("Los jugadores no pueden ser nulos.");
        }

        if (jugadorSale.equals(jugadorEntra)) {
            throw new IllegalArgumentException("Un jugador no puede sustituirse a sí mismo.");
        }

        InscripcionClub inscripcionSale = torneo.buscarInscripcion(jugadorSale);
        InscripcionClub inscripcionEntra = torneo.buscarInscripcion(jugadorEntra);

        if (inscripcionSale == null || inscripcionEntra == null ||
                !inscripcionSale.getClub().equals(inscripcionEntra.getClub())) {
            throw new IllegalArgumentException("Ambos jugadores deben pertenecer al mismo equipo.");
        }

        Club club = inscripcionSale.getClub();

        if (!club.equals(clubLocal) && !club.equals(clubVisitante)) {
            throw new IllegalArgumentException("El jugador no pertenece a ninguno de los equipos del partido.");
        }

        if (!jugadoresEnCancha.contains(jugadorSale)) {
            throw new IllegalArgumentException("El jugador que sale no está en cancha.");
        }

        if (jugadoresEnCancha.contains(jugadorEntra)) {
            throw new IllegalArgumentException("El jugador que entra ya está en cancha.");
        }

        if (jugadoresQueSalieron.contains(jugadorEntra)) {
            throw new IllegalArgumentException("Un jugador que ya salió no puede volver a ingresar.");
        }

        if (jugadorFueExpulsado(jugadorEntra) || torneo.jugadorEstaSuspendido(jugadorEntra)) {
            throw new IllegalArgumentException("Un jugador expulsado o suspendido no puede ingresar.");
        }

        jugadoresEnCancha.remove(jugadorSale);
        jugadoresQueSalieron.add(jugadorSale);
        jugadoresEnCancha.add(jugadorEntra);
    }

    public void cargarTitulares(List<Jugador> titularesLocal, List<Jugador> titularesVisitante) {
        if (estado != EstadoPartido.PROGRAMADO) {
            throw new IllegalStateException("Los titulares deben cargarse antes de iniciar el partido.");
        }

        if (titularesLocal == null || titularesVisitante == null) {
            throw new IllegalArgumentException("Las nóminas no pueden ser nulas.");
        }

        if (titularesLocal.size() != 11 || titularesVisitante.size() != 11) {
            throw new IllegalArgumentException("Cada equipo debe tener 11 titulares.");
        }

        if (torneo == null) {
            throw new IllegalStateException("El partido no tiene un torneo asociado.");
        }

        validarTitulares(titularesLocal, clubLocal);
        validarTitulares(titularesVisitante, clubVisitante);

        List<Jugador> todos = new ArrayList<>(titularesLocal);
        todos.addAll(titularesVisitante);

        if (todos.stream().distinct().count() != 22) {
            throw new IllegalArgumentException("Un jugador no puede estar en ambos equipos ni repetirse.");
        }

        jugadoresEnCancha.clear();
        jugadoresEnCancha.addAll(todos);
    }

    private void validarTitulares(List<Jugador> titulares, Club club) {
        InscripcionClub inscripcion = torneo.buscarInscripcion(club);

        if (inscripcion == null) {
            throw new IllegalStateException("El club no está inscripto en el torneo.");
        }

        for (Jugador jugador : titulares) {
            if (jugador == null || !inscripcion.tieneJugador(jugador)) {
                throw new IllegalArgumentException("Todos los titulares deben pertenecer al club.");
            }

            if (torneo.jugadorEstaSuspendido(jugador)) {
                throw new IllegalArgumentException("Un jugador suspendido no puede ser titular.");
            }
        }
    }

    public void agregarTitularLocal(Jugador jugador) {
        validarTitular(jugador, clubLocal);

        if (jugadoresEnCancha.size() >= 11) {
            throw new IllegalStateException("El equipo local ya tiene 11 titulares.");
        }

        jugadoresEnCancha.add(jugador);
    }

    public void agregarTitularVisitante(Jugador jugador) {
        validarTitular(jugador, clubVisitante);

        long titularesVisitantes = jugadoresEnCancha.stream()
                .filter(j -> torneo.buscarInscripcion(j).getClub().equals(clubVisitante))
                .count();

        if (titularesVisitantes >= 11) {
            throw new IllegalStateException("El equipo visitante ya tiene 11 titulares.");
        }

        if (jugadoresEnCancha.contains(jugador)) {
            throw new IllegalArgumentException("El jugador ya fue seleccionado.");
        }

        jugadoresEnCancha.add(jugador);
    }

    private void validarTitular(Jugador jugador, Club club) {
        if (jugador == null) {
            throw new IllegalArgumentException("El jugador no puede ser nulo.");
        }

        InscripcionClub inscripcion = torneo.buscarInscripcion(jugador);

        if (inscripcion == null || !inscripcion.getClub().equals(club)) {
            throw new IllegalArgumentException("El jugador no pertenece al equipo.");
        }

        if (torneo.jugadorEstaSuspendido(jugador)) {
            throw new IllegalArgumentException("El jugador está suspendido.");
        }

        if (jugadoresEnCancha.contains(jugador)) {
            throw new IllegalArgumentException("El jugador ya fue seleccionado.");
        }
    }

    public boolean jugadorFueExpulsado(Jugador jugador) {
        return jugadoresExpulsados.contains(jugador);
    }

    public int getMarcadorVisitante() {
        return marcadorVisitante;
    }

    public void setMarcadorVisitante(int marcadorVisitante) {
        this.marcadorVisitante = marcadorVisitante;
    }

    public Club getClubVisitante() {
        return clubVisitante;
    }

    public void setClubVisitante(Club clubVisitante) {
        this.clubVisitante = clubVisitante;
    }

    public int getMarcadorLocal() {
        return marcadorLocal;
    }

    public void setMarcadorLocal(int marcadorLocal) {
        this.marcadorLocal = marcadorLocal;
    }

    public Club getClubLocal() {
        return clubLocal;
    }

    public void setClubLocal(Club clubLocal) {
        this.clubLocal = clubLocal;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public EstadoPartido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPartido estado) {
        this.estado = estado;
    }

    public Arbitro getArbitroPrincipal() {
        return arbitroPrincipal;
    }

    public void setArbitroPrincipal(Arbitro arbitroPrincipal) {
        this.arbitroPrincipal = arbitroPrincipal;
    }

    public Arbitro getAsistente1() {
        return asistente1;
    }

    public void setAsistente1(Arbitro asistente1) {
        this.asistente1 = asistente1;
    }

    public Arbitro getAsistente2() {
        return asistente2;
    }

    public void setAsistente2(Arbitro asistente2) {
        this.asistente2 = asistente2;
    }

    public Estadio getEstadio() {
        return estadio;
    }

    public void setEstadio(Estadio estadio) {
        this.estadio = estadio;
    }

    public List<Evento> getEventos() {
        return eventos;
    }

    public void setEventos(List<Evento> eventos) {
        this.eventos = eventos;
    }

    public Torneo getTorneo() {
        return torneo;
    }

    public List<Jugador> getNominaLocal() {
        return new ArrayList<>(nominaLocal);
    }

    public List<Jugador> getNominaVisitante() {
        return new ArrayList<>(nominaVisitante);
    }

    public List<Jugador> getJugadoresExpulsados() {
        return jugadoresExpulsados;
    }

    public List<Jugador> getJugadoresEnCancha() {
        return jugadoresEnCancha;
    }

    public List<Jugador> getJugadoresQueSalieron() {
        return jugadoresQueSalieron;
    }
}