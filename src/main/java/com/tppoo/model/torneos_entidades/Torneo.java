package com.tppoo.model.torneos_entidades;

import com.tppoo.model.torneos_entidades.EstadoTorneo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Torneo {
    private EstadoTorneo estado;
    private String nombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private List<Club> clubes = new ArrayList<>();
    private List<Partido> partidos = new ArrayList<>();
    private List<Sancion> sanciones = new ArrayList<>();
    private List<InscripcionClub> inscripciones = new ArrayList<>();

    public Torneo() {
        this.estado = EstadoTorneo.PLANIFICADO;
    }

    public InscripcionClub inscribirClub(Club club) {

        if (estado != EstadoTorneo.PLANIFICADO) {
            throw new IllegalStateException(
                    "No se pueden inscribir clubes una vez iniciado el torneo."
            );
        }

        if (clubes.contains(club)) {
            throw new IllegalArgumentException(
                    "El club ya está inscripto en este torneo."
            );
        }

        InscripcionClub inscripcion = new InscripcionClub(club, this);

        inscripciones.add(inscripcion);
        clubes.add(club);

        return inscripcion;
    }

    public void agregarInscripcion(InscripcionClub inscripcion) {
        this.inscripciones.add(inscripcion);
    }

    public void generarFixture() {
        if (clubes == null || clubes.size() < 2) {
            System.out.println("No hay suficientes clubes inscriptos para generar el fixture.");
            return;
        }

        List<Club> listaEquipos = new ArrayList<>(clubes);

        boolean esImpar = listaEquipos.size() % 2 != 0;
        if (esImpar) {
            listaEquipos.add(null);
        }

        int totalClubes = listaEquipos.size();
        int totalFechas = totalClubes - 1;
        int partidosPorFecha = totalClubes / 2;

        LocalDate fechaActual;
        if (this.fechaInicio != null) {
            fechaActual = this.fechaInicio;
        } else {
            fechaActual = LocalDate.now();
        }

        for (int f = 0; f < totalFechas; f++) {
            for (int i = 0; i < partidosPorFecha; i++) {
                Club local = listaEquipos.get(i);
                Club visitante = listaEquipos.get(totalClubes - 1 - i);

                if (local != null && visitante != null) {
                    generarPartido(local, visitante, fechaActual);
                }
            }

            Club ultimo = listaEquipos.remove(listaEquipos.size() - 1);
            listaEquipos.add(1, ultimo);

            fechaActual = fechaActual.plusDays(7);
        }

        System.out.println("¡Fixture generado con éxito! Total de partidos creados: " + partidos.size());
    }

    public void avanzarEstado() {
        if (estado == EstadoTorneo.PLANIFICADO) {

            if (inscripciones.isEmpty()) {
                throw new IllegalStateException(
                        "El torneo debe tener clubes inscriptos."
                );
            }

            for (InscripcionClub inscripcion : inscripciones) {

                if (!inscripcion.plantelValido()) {
                    throw new IllegalStateException(
                            "El club " + inscripcion.getClub().getNombre()
                                    + " no tiene un plantel válido (debe tener entre 18 y 30 jugadores)."
                    );
                }
            }

            estado = EstadoTorneo.EN_CURSO;

        } else if (estado == EstadoTorneo.EN_CURSO) {

            estado = EstadoTorneo.FINALIZADO;
        }
    }

    public void generarPartido(Club clubLocal, Club clubVisitante, LocalDate fecha) {
        if (clubLocal != null && clubVisitante != null) {
            Partido nuevoPartido = new Partido(clubLocal, clubVisitante, fecha);
            this.partidos.add(nuevoPartido);
        }
    }

    public void agregarSancion(Sancion sancion) {
        sanciones.add(sancion);
    }

    public List<Sancion> getSanciones() {
        return new ArrayList<>(sanciones);
    }

    public boolean jugadorEstaSuspendido(Jugador jugador) {
        for (Sancion sancion : sanciones) {
            if (sancion.getJugador().equals(jugador)
                    && sancion.estaVigente()) {
                return true;
            }
        }
        return false;
    }

    public EstadoTorneo getEstado() {
        return estado;
    }

    public void setEstado(EstadoTorneo estado) {
        this.estado = estado;
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

    public List<Club> getClubes() {
        return clubes;
    }

    public void setClubes(List<Club> clubes) {
        this.clubes = clubes;
    }

    public List<Partido> getPartidos() {
        return partidos;
    }

    public void setPartidos(List<Partido> partidos) {
        this.partidos = partidos;
    }

    public List<InscripcionClub> getInscripciones() {
        return inscripciones;
    }

    public void setInscripciones(List<InscripcionClub> inscripciones) {
        this.inscripciones = inscripciones;
    }
}