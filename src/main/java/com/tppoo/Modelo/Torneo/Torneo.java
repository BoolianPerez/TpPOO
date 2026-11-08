package com.tppoo.Modelo.Torneo;

import com.tppoo.Modelo.Club.Club;
import com.tppoo.Modelo.Club.Estadio;
import com.tppoo.Modelo.Club.Patrocinio;
import com.tppoo.Modelo.Excepcion.EstadioNoDisponibleException;
import com.tppoo.Modelo.Excepcion.PlantelInvalidoException;
import com.tppoo.Modelo.Excepcion.ReglaNegocioException;
import com.tppoo.Modelo.Excepcion.TransicionEstadoInvalidaException;
import com.tppoo.Modelo.Persona.Arbitro;
import com.tppoo.Modelo.Persona.Jugador;
import com.tppoo.Modelo.Partido.EstadoPartido;
import com.tppoo.Modelo.Partido.Partido;
import com.tppoo.Modelo.Partido.TernaArbitral;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Torneo implements Serializable {
    public static final int MIN_CLUBES = 4;
    public static final long MINUTOS_DESCANSO_CLUB = 48 * 60;
    private static final int HORA_PARTIDO = 16;
    private static final int DIAS_ENTRE_FECHAS = 7;

    private EstadoTorneo estado = EstadoTorneo.PLANIFICADO;
    private String nombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private boolean internacional;
    private final List<Partido> partidos = new ArrayList<>();
    private final List<Sancion> sanciones = new ArrayList<>();
    private final List<InscripcionClub> inscripciones = new ArrayList<>();
    private final List<Patrocinio> patrocinios = new ArrayList<>();

    public Torneo() {
    }

    public Torneo(String nombre, LocalDate fechaInicio, LocalDate fechaFin, boolean internacional) {
        if (fechaInicio == null || fechaFin == null || fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("El torneo requiere fechas válidas (fin no anterior al inicio).");
        }
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.internacional = internacional;
    }

    public InscripcionClub inscribirClub(Club club) {
        if (club == null) {
            throw new IllegalArgumentException("El club no puede ser null.");
        }
        if (estado != EstadoTorneo.PLANIFICADO) {
            throw new TransicionEstadoInvalidaException("No se pueden inscribir clubes una vez iniciado el torneo.");
        }
        if (buscarInscripcion(club) != null) { // compara por nombre (Club.mismoNombre)
            throw new ReglaNegocioException("El club ya está inscripto en este torneo.");
        }
        InscripcionClub inscripcion = new InscripcionClub(club, this);
        inscripciones.add(inscripcion);
        return inscripcion;
    }

    public void avanzarEstado() {
        if (estado == EstadoTorneo.PLANIFICADO) {
            if (inscripciones.size() < MIN_CLUBES) {
                throw new TransicionEstadoInvalidaException(
                        "El torneo necesita al menos " + MIN_CLUBES + " clubes inscriptos para iniciar.");
            }
            for (InscripcionClub inscripcion : inscripciones) {
                if (!inscripcion.plantelValido()) {
                    throw new PlantelInvalidoException("El club " + inscripcion.getClub().getNombre()
                            + " no tiene un plantel válido (debe tener entre 18 y 30 jugadores).");
                }
            }
            estado = EstadoTorneo.EN_CURSO;
        } else if (estado == EstadoTorneo.EN_CURSO) {
            for (Partido partido : partidos) {
                if (partido.getEstado() == EstadoPartido.PROGRAMADO || partido.getEstado() == EstadoPartido.EN_JUEGO) {
                    throw new TransicionEstadoInvalidaException(
                            "No se puede finalizar el torneo: hay partidos sin terminar.");
                }
            }
            estado = EstadoTorneo.FINALIZADO;
        } else {
            throw new TransicionEstadoInvalidaException("El torneo ya está finalizado.");
        }
    }


    public void generarFixture() {
        if (estado != EstadoTorneo.PLANIFICADO) {
            throw new TransicionEstadoInvalidaException("El fixture solo puede generarse con el torneo planificado.");
        }
        if (!partidos.isEmpty()) {
            throw new ReglaNegocioException("El fixture ya fue generado.");
        }
        if (inscripciones.size() < MIN_CLUBES) {
            throw new ReglaNegocioException("Se necesitan al menos " + MIN_CLUBES + " clubes inscriptos para generar el fixture.");
        }
        if (fechaInicio == null || fechaFin == null) {
            throw new ReglaNegocioException("El torneo necesita fecha de inicio y de fin para generar el fixture.");
        }

        List<Club> equipos = getClubes();
        if (equipos.size() % 2 != 0) {
            equipos.add(null); // fecha libre
        }
        int totalClubes = equipos.size();
        int totalFechas = totalClubes - 1;
        int partidosPorFecha = totalClubes / 2;

        LocalDate ultimaFecha = fechaInicio.plusDays((long) DIAS_ENTRE_FECHAS * (totalFechas - 1));
        if (ultimaFecha.isAfter(fechaFin)) {
            throw new ReglaNegocioException("El fixture no entra en el período del torneo.");
        }

        LocalDate fechaActual = fechaInicio;
        for (int f = 0; f < totalFechas; f++) {
            for (int i = 0; i < partidosPorFecha; i++) {
                Club a = equipos.get(i);
                Club b = equipos.get(totalClubes - 1 - i);
                if (a != null && b != null) {
                    // se alterna la localía entre fechas
                    if (f % 2 == 0) {
                        generarPartido(a, b, fechaActual.atTime(HORA_PARTIDO, 0));
                    } else {
                        generarPartido(b, a, fechaActual.atTime(HORA_PARTIDO, 0));
                    }
                }
            }
            Club ultimo = equipos.remove(equipos.size() - 1);
            equipos.add(1, ultimo);
            fechaActual = fechaActual.plusDays(DIAS_ENTRE_FECHAS);
        }
    }

    private void generarPartido(Club local, Club visitante, LocalDateTime fechaHora) {
        Partido partido = new Partido(local, visitante, fechaHora, this);
        if (local.getEstadioLocal() != null) {
            try {
                partido.asignarEstadio(local.getEstadioLocal());
            } catch (EstadioNoDisponibleException e) {
                // el partido queda sin estadio y la administración debe asignarlo a mano
            }
        }
        partidos.add(partido);
    }

    public Partido programarPartido(Club local, Club visitante, LocalDateTime fechaHora,
                                    Estadio estadio, TernaArbitral terna) {
        if (estado == EstadoTorneo.FINALIZADO) {
            throw new TransicionEstadoInvalidaException("El torneo está finalizado.");
        }
        if (buscarInscripcion(local) == null || buscarInscripcion(visitante) == null) {
            throw new ReglaNegocioException("Ambos clubes deben estar inscriptos en el torneo.");
        }
        validarFechaEnPeriodo(fechaHora);
        validarDescanso(local, fechaHora, null);
        validarDescanso(visitante, fechaHora, null);

        Partido partido = new Partido(local, visitante, fechaHora, this);
        partido.asignarTerna(terna);
        partido.asignarEstadio(estadio);
        partidos.add(partido);
        return partido;
    }

    public void validarFechaEnPeriodo(LocalDateTime fechaHora) {
        if (fechaHora == null) {
            throw new IllegalArgumentException("La fecha no puede ser null.");
        }
        LocalDate dia = fechaHora.toLocalDate();
        if (fechaInicio != null && dia.isBefore(fechaInicio) || fechaFin != null && dia.isAfter(fechaFin)) {
            throw new ReglaNegocioException("La fecha del partido debe estar dentro del período del torneo.");
        }
    }

    public void validarDescanso(Club club, LocalDateTime fechaHora, Partido ignorar) {
        for (Partido p : partidos) {
            if (p == ignorar || p.getEstado() == EstadoPartido.SUSPENDIDO) {
                continue;
            }
            if (club.equals(p.getClubLocal()) || club.equals(p.getClubVisitante())) {
                long minutos = Math.abs(ChronoUnit.MINUTES.between(p.getFechaHora(), fechaHora));
                if (minutos < MINUTOS_DESCANSO_CLUB) {
                    throw new ReglaNegocioException("El club " + club.getNombre()
                            + " no puede jugar dos partidos con menos de 48 horas de diferencia.");
                }
            }
        }
    }

    public void validarTerna(Partido partido, TernaArbitral terna) {
        if (internacional && !terna.todosInternacionales()) {
            throw new ReglaNegocioException("Los torneos internacionales exigen una terna de categoría internacional.");
        }
        LocalDate dia = partido.getFechaHora().toLocalDate();
        for (Arbitro arbitro : terna.getArbitros()) {
            List<LocalDate> fechas = fechasAsignadas(arbitro, partido);
            if (!arbitro.verificarDisponibilidad(dia, partido.getClubLocal(), partido.getClubVisitante(), fechas)) {
                throw new ReglaNegocioException("El árbitro " + arbitro.obtenerDetalle()
                        + " no está disponible (es hincha de un club del partido o ya dirige ese día).");
            }
        }
    }

    private List<LocalDate> fechasAsignadas(Arbitro arbitro, Partido excluir) {
        List<LocalDate> fechas = new ArrayList<>();
        for (Partido p : partidos) {
            if (p != excluir && p.getTerna() != null && p.getTerna().contiene(arbitro)) {
                fechas.add(p.getFechaHora().toLocalDate());
            }
        }
        return fechas;
    }

    public void agregarSancion(Sancion sancion) {
        if (sancion == null || sancion.getTorneo() != this) {
            throw new IllegalArgumentException("La sanción no pertenece a este torneo.");
        }
        sanciones.add(sancion);
    }

    public void quitarSancion(Sancion sancion) {
        sanciones.remove(sancion);
    }

    public List<Sancion> getSanciones() {
        return new ArrayList<>(sanciones);
    }

    public boolean jugadorEstaSuspendido(Jugador jugador) {
        if (jugador == null) {
            return false;
        }
        for (Sancion sancion : sanciones) {
            if (sancion.getJugador().equals(jugador) && sancion.estaVigente()) {
                return true;
            }
        }
        return false;
    }

    public void cumplirSancionesDelPartido(Partido partido) {
        if (partido.getEstado() != EstadoPartido.FINALIZADO) {
            throw new TransicionEstadoInvalidaException("Solo se pueden cumplir sanciones al finalizar un partido.");
        }
        for (Sancion sancion : sanciones) {
            if (!sancion.estaVigente() || sancion.getPartidoOrigen() == partido) {
                continue;
            }
            InscripcionClub inscripcion = buscarInscripcion(sancion.getJugador());
            if (inscripcion == null) {
                continue;
            }
            Club clubJugador = inscripcion.getClub();
            if (clubJugador.equals(partido.getClubLocal()) || clubJugador.equals(partido.getClubVisitante())) {
                sancion.cumplirPartido();
            }
        }
    }

    public void agregarPatrocinio(Patrocinio patrocinio) {
        Patrocinio.validarNuevo(patrocinios, patrocinio, false);
        patrocinios.add(patrocinio);
    }

    public List<Patrocinio> getPatrocinios() {
        return Collections.unmodifiableList(patrocinios);
    }

    public InscripcionClub buscarInscripcion(Jugador jugador) {
        if (jugador == null) {
            return null;
        }
        for (InscripcionClub inscripcion : inscripciones) {
            if (inscripcion.tieneJugador(jugador)) {
                return inscripcion;
            }
        }
        return null;
    }

    public InscripcionClub buscarInscripcion(Club club) {
        if (club == null) {
            return null;
        }
        for (InscripcionClub inscripcion : inscripciones) {
            if (inscripcion.getClub() == club || inscripcion.getClub().mismoNombre(club)) {
                return inscripcion;
            }
        }
        return null;
    }

    public EstadoTorneo getEstado() {
        return estado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public boolean isInternacional() {
        return internacional;
    }

    public void setInternacional(boolean internacional) {
        this.internacional = internacional;
    }

    public List<Club> getClubes() {
        List<Club> clubes = new ArrayList<>();
        for (InscripcionClub inscripcion : inscripciones) {
            clubes.add(inscripcion.getClub());
        }
        return clubes;
    }

    public List<Partido> getPartidos() {
        return Collections.unmodifiableList(partidos);
    }

    public List<InscripcionClub> getInscripciones() {
        return Collections.unmodifiableList(inscripciones);
    }
}
