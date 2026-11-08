package com.tppoo.model.torneos_entidades;

import com.tppoo.model.excepciones.EventoInvalidoException;
import com.tppoo.model.excepciones.JugadorSuspendidoException;
import com.tppoo.model.excepciones.LimiteCambiosExcedidoException;
import com.tppoo.model.excepciones.PlantelInvalidoException;
import com.tppoo.model.excepciones.ReglaNegocioException;
import com.tppoo.model.excepciones.TransicionEstadoInvalidaException;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Partido implements Serializable {
    public static final int MAX_CAMBIOS = 5;
    public static final int MINUTO_MAXIMO = 120;
    private static final int TITULARES_POR_EQUIPO = 11;

    private final Club clubLocal;
    private final Club clubVisitante;
    private final Torneo torneo;
    private LocalDateTime fechaHora;
    private EstadoPartido estado = EstadoPartido.PROGRAMADO;
    private TernaArbitral terna;
    private Estadio estadio;
    private int marcadorLocal = 0;
    private int marcadorVisitante = 0;

    private final List<Evento> eventos = new ArrayList<>();
    private final List<Jugador> jugadoresExpulsados = new ArrayList<>();
    private final List<Jugador> nominaLocal = new ArrayList<>();
    private final List<Jugador> nominaVisitante = new ArrayList<>();
    private final List<Jugador> jugadoresEnCancha = new ArrayList<>();
    private final List<Jugador> jugadoresQueSalieron = new ArrayList<>();
    private final Map<Club, Integer> cambiosPorClub = new HashMap<>();
    private final List<ObservadorPartido> observadores = new ArrayList<>();
    private int fallosDeObservadores = 0;

    public Partido(Club clubLocal, Club clubVisitante, LocalDateTime fechaHora, Torneo torneo) {
        if (clubLocal == null || clubVisitante == null || torneo == null || fechaHora == null) {
            throw new IllegalArgumentException("El partido requiere clubes, fecha/hora y torneo.");
        }
        if (clubLocal.equals(clubVisitante)) {
            throw new IllegalArgumentException("Un club no puede jugar contra sí mismo.");
        }
        this.clubLocal = clubLocal;
        this.clubVisitante = clubVisitante;
        this.fechaHora = fechaHora;
        this.torneo = torneo;
    }

    // ------------------------------------------------------------------ programación

    public void asignarTerna(TernaArbitral nuevaTerna) {
        exigirProgramado("asignar la terna");
        if (nuevaTerna == null) {
            throw new IllegalArgumentException("La terna no puede ser null.");
        }
        torneo.validarTerna(this, nuevaTerna);
        this.terna = nuevaTerna;
    }

    /** Asigna el estadio reservándolo (RN03). Si falla, el estadio anterior se conserva. */
    public void asignarEstadio(Estadio nuevoEstadio) {
        exigirProgramado("asignar el estadio");
        if (nuevoEstadio == null) {
            throw new IllegalArgumentException("El estadio no puede ser null.");
        }
        nuevoEstadio.reservar(this);
        if (this.estadio != null && this.estadio != nuevoEstadio) {
            this.estadio.liberar(this);
        }
        this.estadio = nuevoEstadio;
    }

    /** Cambia la fecha revalidando período del torneo, descanso de los clubes, estadio y árbitros. */
    public void reprogramar(LocalDateTime nuevaFechaHora) {
        exigirProgramado("reprogramar");
        if (nuevaFechaHora == null) {
            throw new IllegalArgumentException("La fecha no puede ser null.");
        }
        torneo.validarFechaEnPeriodo(nuevaFechaHora);
        torneo.validarDescanso(clubLocal, nuevaFechaHora, this);
        torneo.validarDescanso(clubVisitante, nuevaFechaHora, this);
        if (estadio != null && !estadio.validarDisponibilidadHoraria(nuevaFechaHora, this)) {
            throw new com.tppoo.model.excepciones.EstadioNoDisponibleException(
                    "RN03: El estadio no está disponible en la nueva fecha.");
        }
        LocalDateTime anterior = this.fechaHora;
        this.fechaHora = nuevaFechaHora;
        if (terna != null) {
            try {
                torneo.validarTerna(this, terna);
            } catch (ReglaNegocioException e) {
                this.fechaHora = anterior;
                throw e;
            }
        }
    }

    private void exigirProgramado(String accion) {
        if (estado != EstadoPartido.PROGRAMADO) {
            throw new TransicionEstadoInvalidaException("Solo se puede " + accion + " en un partido programado.");
        }
    }

    // ------------------------------------------------------------------ nómina y titulares

    public void prepararPartido() {
        exigirProgramado("preparar la nómina");
        nominaLocal.clear();
        nominaVisitante.clear();
        cargarNominaLocal();
        cargarNominaVisitante();
    }

    public void cargarNominaLocal() {
        InscripcionClub inscripcion = torneo.buscarInscripcion(clubLocal);
        if (inscripcion == null) {
            throw new ReglaNegocioException("El club local no está inscripto en el torneo.");
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
            throw new ReglaNegocioException("El club visitante no está inscripto en el torneo.");
        }
        for (Jugador jugador : inscripcion.getJugadores()) {
            if (!torneo.jugadorEstaSuspendido(jugador)) {
                agregarJugadorNominaVisitante(jugador);
            }
        }
    }

    public void agregarJugadorNominaLocal(Jugador jugador) {
        validarJugadorNomina(jugador, clubLocal, nominaLocal);
    }

    public void agregarJugadorNominaVisitante(Jugador jugador) {
        validarJugadorNomina(jugador, clubVisitante, nominaVisitante);
    }

    private void validarJugadorNomina(Jugador jugador, Club club, List<Jugador> nomina) {
        exigirProgramado("modificar la nómina");
        if (jugador == null) {
            throw new IllegalArgumentException("El jugador no puede ser null.");
        }
        InscripcionClub inscripcion = torneo.buscarInscripcion(jugador);
        if (inscripcion == null || !club.equals(inscripcion.getClub())) {
            throw new PlantelInvalidoException("El jugador no pertenece a este equipo.");
        }
        if (torneo.jugadorEstaSuspendido(jugador)) {
            throw new JugadorSuspendidoException("El jugador " + jugador.obtenerDetalle() + " está suspendido.");
        }
        if (nomina.contains(jugador)) {
            throw new IllegalArgumentException("El jugador ya está incluido en la nómina.");
        }
        nomina.add(jugador);
    }

    /** Única forma de cargar los 22 titulares (11 por equipo). */
    public void cargarTitulares(List<Jugador> titularesLocal, List<Jugador> titularesVisitante) {
        exigirProgramado("cargar los titulares");
        if (titularesLocal == null || titularesVisitante == null) {
            throw new IllegalArgumentException("Las nóminas no pueden ser nulas.");
        }
        if (titularesLocal.size() != TITULARES_POR_EQUIPO || titularesVisitante.size() != TITULARES_POR_EQUIPO) {
            throw new PlantelInvalidoException("Cada equipo debe tener 11 titulares.");
        }
        validarTitulares(titularesLocal, clubLocal);
        validarTitulares(titularesVisitante, clubVisitante);

        List<Jugador> todos = new ArrayList<>(titularesLocal);
        todos.addAll(titularesVisitante);
        if (todos.stream().distinct().count() != 2L * TITULARES_POR_EQUIPO) {
            throw new PlantelInvalidoException("Un jugador no puede estar en ambos equipos ni repetirse.");
        }
        jugadoresEnCancha.clear();
        jugadoresEnCancha.addAll(todos);
    }

    private void validarTitulares(List<Jugador> titulares, Club club) {
        InscripcionClub inscripcion = torneo.buscarInscripcion(club);
        if (inscripcion == null) {
            throw new ReglaNegocioException("El club " + club.getNombre() + " no está inscripto en el torneo.");
        }
        for (Jugador jugador : titulares) {
            if (jugador == null || !inscripcion.tieneJugador(jugador)) {
                throw new PlantelInvalidoException("Todos los titulares deben pertenecer al club.");
            }
            if (torneo.jugadorEstaSuspendido(jugador)) {
                throw new JugadorSuspendidoException("Un jugador suspendido no puede ser titular.");
            }
        }
    }

    // ------------------------------------------------------------------ ciclo de vida

    public void iniciar() {
        if (estado != EstadoPartido.PROGRAMADO) {
            throw new TransicionEstadoInvalidaException("El partido no está programado.");
        }
        if (torneo.getEstado() != EstadoTorneo.EN_CURSO) {
            throw new TransicionEstadoInvalidaException("El torneo debe estar en curso para iniciar un partido.");
        }
        if (estadio == null) {
            throw new ReglaNegocioException("El partido necesita un estadio asignado.");
        }
        if (terna == null) {
            throw new ReglaNegocioException("El partido necesita una terna arbitral asignada.");
        }
        if (jugadoresEnCancha.size() != 2 * TITULARES_POR_EQUIPO) {
            throw new PlantelInvalidoException("Deben cargarse los titulares de ambos equipos.");
        }
        if (nominaLocal.isEmpty()) {
            cargarNominaLocal();
        }
        if (nominaVisitante.isEmpty()) {
            cargarNominaVisitante();
        }
        estado = EstadoPartido.EN_JUEGO;
    }

    public void suspender() {
        if (estado == EstadoPartido.FINALIZADO || estado == EstadoPartido.SUSPENDIDO) {
            throw new TransicionEstadoInvalidaException("El partido ya está " + estado + ".");
        }
        estado = EstadoPartido.SUSPENDIDO;
    }

    /**
     * Registra un evento. Se procesa ANTES de guardarlo: si el procesamiento falla,
     * el evento no queda registrado.
     */
    public void registrarEvento(Evento evento) {
        if (evento == null) {
            throw new IllegalArgumentException("El evento no puede ser null.");
        }
        if (estado != EstadoPartido.EN_JUEGO) {
            throw new EventoInvalidoException("RN08: Los eventos únicamente pueden registrarse cuando el partido está EN_JUEGO.");
        }
        if (evento.getMinuto() < 0 || evento.getMinuto() > MINUTO_MAXIMO) {
            throw new EventoInvalidoException("RN08: El minuto del evento debe estar entre 0 y 120.");
        }
        if (evento.getPartido() != this) {
            throw new IllegalArgumentException("El evento no pertenece a este partido.");
        }
        if (eventos.contains(evento)) {
            throw new IllegalArgumentException("El evento ya fue registrado en este partido.");
        }

        if (evento.exigeJugadorActivo()) {
            Jugador jugador = evento.getJugadorInvolucrado();
            if (jugador == null) {
                throw new EventoInvalidoException("El evento debe tener un jugador involucrado.");
            }
            if (jugadorFueExpulsado(jugador)) {
                throw new EventoInvalidoException("El jugador fue expulsado y no puede seguir participando.");
            }
            if (!jugadoresEnCancha.contains(jugador)) {
                throw new EventoInvalidoException("RN09: El jugador no está en cancha (no está en la nómina activa del partido).");
            }
        }

        evento.procesar();
        eventos.add(evento);
        notificarObservadores(evento);
    }

    private void notificarObservadores(Evento evento) {
        for (ObservadorPartido observador : new ArrayList<>(observadores)) {
            try {
                evento.notificarA(observador);
            } catch (RuntimeException e) {
                // RN13: un fallo al notificar nunca frena la carga del partido.
                fallosDeObservadores++;
            }
        }
    }

    public void finalizar() {
        if (estado != EstadoPartido.EN_JUEGO) {
            throw new TransicionEstadoInvalidaException("Solo se puede finalizar un partido que esté EN_JUEGO.");
        }
        estado = EstadoPartido.FINALIZADO;
        torneo.cumplirSancionesDelPartido(this);

        Set<Jugador> participantes = new LinkedHashSet<>(jugadoresEnCancha);
        participantes.addAll(jugadoresQueSalieron);
        participantes.addAll(jugadoresExpulsados);
        for (Jugador jugador : participantes) {
            jugador.incrementarPartidosJugados();
        }

        for (ObservadorPartido observador : new ArrayList<>(observadores)) {
            try {
                observador.onFinPartido(this);
            } catch (RuntimeException e) {
                fallosDeObservadores++;
            }
        }
    }

    // ------------------------------------------------------------------ expulsiones y cambios

    public void expulsarJugador(Jugador jugador) {
        if (estado != EstadoPartido.EN_JUEGO) {
            throw new TransicionEstadoInvalidaException("Solo se puede expulsar a un jugador durante un partido en juego.");
        }
        if (jugador == null) {
            throw new IllegalArgumentException("El jugador no puede ser null.");
        }
        if (!jugadoresExpulsados.contains(jugador)) {
            jugadoresExpulsados.add(jugador);
        }
        // El club queda con un jugador menos y el expulsado no puede ser sustituido.
        jugadoresEnCancha.remove(jugador);
    }

    /** Deshace una expulsión (solo la usa la anulación de la tarjeta). */
    void readmitirJugador(Jugador jugador) {
        if (jugadoresExpulsados.remove(jugador) && !jugadoresEnCancha.contains(jugador)) {
            jugadoresEnCancha.add(jugador);
        }
    }

    public void registrarCambio(Jugador jugadorSale, Jugador jugadorEntra) {
        if (estado != EstadoPartido.EN_JUEGO) {
            throw new TransicionEstadoInvalidaException("El partido no está en juego.");
        }
        if (jugadorSale == null || jugadorEntra == null) {
            throw new IllegalArgumentException("Los jugadores no pueden ser nulos.");
        }
        if (jugadorSale.equals(jugadorEntra)) {
            throw new EventoInvalidoException("Un jugador no puede sustituirse a sí mismo.");
        }

        Club club = clubDe(jugadorSale);
        if (club == null || !club.equals(clubDe(jugadorEntra))) {
            throw new EventoInvalidoException("Ambos jugadores deben pertenecer al mismo equipo.");
        }
        if (!club.equals(clubLocal) && !club.equals(clubVisitante)) {
            throw new EventoInvalidoException("El jugador no pertenece a ninguno de los equipos del partido.");
        }
        if (jugadorFueExpulsado(jugadorSale)) {
            throw new EventoInvalidoException("RN10: Un jugador expulsado no puede ser sustituido.");
        }
        if (!jugadoresEnCancha.contains(jugadorSale)) {
            throw new EventoInvalidoException("El jugador que sale no está en cancha.");
        }
        if (jugadoresEnCancha.contains(jugadorEntra)) {
            throw new EventoInvalidoException("El jugador que entra ya está en cancha.");
        }
        if (jugadoresQueSalieron.contains(jugadorEntra)) {
            throw new EventoInvalidoException("RN09: Un jugador que ya salió no puede volver a ingresar.");
        }
        if (jugadorFueExpulsado(jugadorEntra) || torneo.jugadorEstaSuspendido(jugadorEntra)) {
            throw new EventoInvalidoException("Un jugador expulsado o suspendido no puede ingresar.");
        }
        List<Jugador> nomina = club.equals(clubLocal) ? nominaLocal : nominaVisitante;
        if (!nomina.contains(jugadorEntra)) {
            throw new EventoInvalidoException("RN09: El jugador que entra no está en la nómina del partido.");
        }

        int usados = cambiosPorClub.getOrDefault(club, 0);
        if (usados >= MAX_CAMBIOS) {
            throw new LimiteCambiosExcedidoException(
                    "El club " + club.getNombre() + " ya realizó los " + MAX_CAMBIOS + " cambios permitidos.");
        }

        jugadoresEnCancha.remove(jugadorSale);
        jugadoresQueSalieron.add(jugadorSale);
        jugadoresEnCancha.add(jugadorEntra);
        cambiosPorClub.put(club, usados + 1);
    }

    /** Deshace un cambio (solo la usa la anulación del evento Cambio). */
    void deshacerCambio(Jugador jugadorSale, Jugador jugadorEntra) {
        if (!jugadoresEnCancha.contains(jugadorEntra)) {
            throw new EventoInvalidoException("No se puede anular el cambio: el jugador que entró ya no está en cancha.");
        }
        jugadoresEnCancha.remove(jugadorEntra);
        jugadoresEnCancha.add(jugadorSale);
        jugadoresQueSalieron.remove(jugadorSale);
        Club club = clubDe(jugadorSale);
        cambiosPorClub.merge(club, -1, Integer::sum);
    }

    public int getCambiosRealizados(Club club) {
        return cambiosPorClub.getOrDefault(club, 0);
    }

    // ------------------------------------------------------------------ marcador (solo lo modifican los goles)

    void sumarGolLocal() {
        marcadorLocal++;
    }

    void sumarGolVisitante() {
        marcadorVisitante++;
    }

    void restarGolLocal() {
        marcadorLocal--;
    }

    void restarGolVisitante() {
        marcadorVisitante--;
    }

    // ------------------------------------------------------------------ observadores

    public void agregarObservador(ObservadorPartido observador) {
        if (observador != null && !observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    public void quitarObservador(ObservadorPartido observador) {
        observadores.remove(observador);
    }

    public int getFallosDeObservadores() {
        return fallosDeObservadores;
    }

    // ------------------------------------------------------------------ consultas

    /** Club al que pertenece el jugador en el torneo del partido, o null. */
    public Club clubDe(Jugador jugador) {
        InscripcionClub inscripcion = torneo.buscarInscripcion(jugador);
        return inscripcion == null ? null : inscripcion.getClub();
    }

    public boolean jugadorFueExpulsado(Jugador jugador) {
        return jugadoresExpulsados.contains(jugador);
    }

    public int getMarcadorLocal() {
        return marcadorLocal;
    }

    public int getMarcadorVisitante() {
        return marcadorVisitante;
    }

    public Club getClubLocal() {
        return clubLocal;
    }

    public Club getClubVisitante() {
        return clubVisitante;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public EstadoPartido getEstado() {
        return estado;
    }

    public TernaArbitral getTerna() {
        return terna;
    }

    public Estadio getEstadio() {
        return estadio;
    }

    public Torneo getTorneo() {
        return torneo;
    }

    public List<Evento> getEventos() {
        return Collections.unmodifiableList(eventos);
    }

    public List<Jugador> getNominaLocal() {
        return new ArrayList<>(nominaLocal);
    }

    public List<Jugador> getNominaVisitante() {
        return new ArrayList<>(nominaVisitante);
    }

    public List<Jugador> getJugadoresExpulsados() {
        return Collections.unmodifiableList(jugadoresExpulsados);
    }

    public List<Jugador> getJugadoresEnCancha() {
        return Collections.unmodifiableList(jugadoresEnCancha);
    }

    public List<Jugador> getJugadoresQueSalieron() {
        return Collections.unmodifiableList(jugadoresQueSalieron);
    }
}
