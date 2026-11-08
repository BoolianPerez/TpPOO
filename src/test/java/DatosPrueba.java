import com.tppoo.Modelo.Club.Club;
import com.tppoo.Modelo.Club.Estadio;
import com.tppoo.Modelo.Persona.Arbitro;
import com.tppoo.Modelo.Persona.CategoriaArbitro;
import com.tppoo.Modelo.Persona.Jugador;
import com.tppoo.Modelo.Torneo.InscripcionClub;
import com.tppoo.Modelo.Torneo.Torneo;
import com.tppoo.Modelo.Partido.Partido;
import com.tppoo.Modelo.Partido.TernaArbitral;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

final class DatosPrueba {
    private static int dni = 1000;

    private DatosPrueba() {}

    static Torneo torneoConCuatroClubes() {
        Torneo torneo = new Torneo("Liga de prueba",
                LocalDate.of(2026, 3, 1), LocalDate.of(2026, 6, 30), false);
        clubConPlantel(torneo, "Alfa", 18);
        clubConPlantel(torneo, "Beta", 18);
        clubConPlantel(torneo, "Gama", 18);
        clubConPlantel(torneo, "Delta", 18);
        return torneo;
    }

    static Club clubConPlantel(Torneo torneo, String nombre, int cantidad) {
        Club club = new Club(nombre, LocalDate.of(1900, 1, 1), "Sede " + nombre);
        InscripcionClub inscripcion = torneo.inscribirClub(club);

        for (int i = 0; i < cantidad; i++) {
            Jugador jugador = new Jugador(String.valueOf(dni++), "Jugador" + i,
                    nombre + i, LocalDate.of(2000, 1, 1), "MED", i + 1, 1000);
            club.contratar(jugador, LocalDate.of(2025, 1, 1), null);
            inscripcion.agregarJugador(jugador);
        }
        return club;
    }

    static Arbitro arbitro(String nombre, CategoriaArbitro categoria) {
        return new Arbitro(String.valueOf(dni++), nombre, "Arbitro",
                LocalDate.of(1980, 1, 1), "Colegio", categoria);
    }

    static Estadio estadio(String nombre) {
        return new Estadio(nombre, 50000, "Avenida", 1, "Ciudad", "Provincia");
    }

    static TernaArbitral ternaValida() {
        return new TernaArbitral(
                arbitro("Principal", CategoriaArbitro.NACIONAL),
                arbitro("Asistente 1", CategoriaArbitro.NACIONAL),
                arbitro("Asistente 2", CategoriaArbitro.NACIONAL)
        );
    }

    static Partido partidoEnJuego(Torneo torneo) {
        torneo.generarFixture();
        torneo.avanzarEstado();
        Partido partido = torneo.getPartidos().get(0);
        partido.asignarTerna(ternaValida());
        partido.asignarEstadio(estadio("Estadio de prueba"));

        Club local = partido.getClubLocal();
        Club visitante = partido.getClubVisitante();
        List<Jugador> jugadoresLocal = torneo.buscarInscripcion(local).getJugadores();
        List<Jugador> jugadoresVisitante = torneo.buscarInscripcion(visitante).getJugadores();

        partido.cargarTitulares(
                new ArrayList<>(jugadoresLocal.subList(0, 11)),
                new ArrayList<>(jugadoresVisitante.subList(0, 11)));
        partido.iniciar();
        return partido;
    }
}
