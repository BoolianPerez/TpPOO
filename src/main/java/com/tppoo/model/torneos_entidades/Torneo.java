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

    public Torneo() {
        this.estado = EstadoTorneo.PLANIFICADO;
    }

    public void inscribirClub(Club club) {
        if (club != null && !clubes.contains(club)) {
            this.clubes.add(club);
        }
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
        if (this.estado == EstadoTorneo.PLANIFICADO) {
            this.estado = EstadoTorneo.EN_CURSO;
            System.out.println("El torneo ha comenzado (Estado: EN_CURSO).");
        } else if (this.estado == EstadoTorneo.EN_CURSO) {
            this.estado = EstadoTorneo.FINALIZADO;
            System.out.println("El torneo ha finalizado (Estado: FINALIZADO).");
        } else {
            System.out.println("El torneo ya se encuentra finalizado.");
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
}