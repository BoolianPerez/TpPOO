import com.tppoo.Modelo.Excepcion.EventoInvalidoException;
import com.tppoo.Modelo.Partido.*;
import com.tppoo.Modelo.Persona.Jugador;
import com.tppoo.Modelo.Torneo.Torneo;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EventoPartidoTest {

    @Test
    void registrarGolActualizaElMarcador() {
        Torneo torneo = DatosPrueba.torneoConCuatroClubes();
        Partido partido = DatosPrueba.partidoEnJuego(torneo);
        Jugador goleador = torneo.buscarInscripcion(partido.getClubLocal()).getJugadores().get(0);

        partido.registrarEvento(new Gol(goleador, partido, 10, TipoGol.A_FAVOR));

        assertEquals(1, partido.getMarcadorLocal());
        assertEquals(0, partido.getMarcadorVisitante());
    }

    @Test
    void noPermiteEventoFueraDelRangoDeMinutos() {
        Torneo torneo = DatosPrueba.torneoConCuatroClubes();
        Partido partido = DatosPrueba.partidoEnJuego(torneo);
        Jugador jugador = torneo.buscarInscripcion(partido.getClubLocal()).getJugadores().get(0);

        assertThrows(EventoInvalidoException.class,
                () -> partido.registrarEvento(new Falta(jugador, partido, 121)));
    }

    @Test
    void noPermiteEventoDeJugadorQueEstaEnElBanco() {
        Torneo torneo = DatosPrueba.torneoConCuatroClubes();
        Partido partido = DatosPrueba.partidoEnJuego(torneo);
        Jugador suplente = torneo.buscarInscripcion(partido.getClubLocal()).getJugadores().get(12);

        assertThrows(EventoInvalidoException.class,
                () -> partido.registrarEvento(new Falta(suplente, partido, 30)));
    }

    @Test
    void tarjetaRojaExpulsaAlJugadorYGeneraSuspension() {
        Torneo torneo = DatosPrueba.torneoConCuatroClubes();
        Partido partido = DatosPrueba.partidoEnJuego(torneo);
        Jugador jugador = torneo.buscarInscripcion(partido.getClubVisitante()).getJugadores().get(0);

        partido.registrarEvento(new Tarjeta(jugador, partido, 40, ColorTarjeta.ROJA));

        assertTrue(partido.jugadorFueExpulsado(jugador));
        assertFalse(partido.getJugadoresEnCancha().contains(jugador));
        assertTrue(torneo.jugadorEstaSuspendido(jugador));
    }

    @Test
    void dobleAmarillaExpulsaAlJugador() {
        Torneo torneo = DatosPrueba.torneoConCuatroClubes();
        Partido partido = DatosPrueba.partidoEnJuego(torneo);
        Jugador jugador = torneo.buscarInscripcion(partido.getClubVisitante()).getJugadores().get(0);

        partido.registrarEvento(new Tarjeta(jugador, partido, 40, ColorTarjeta.AMARILLA));
        partido.registrarEvento(new Tarjeta(jugador, partido, 60, ColorTarjeta.AMARILLA));

        assertTrue(partido.jugadorFueExpulsado(jugador));
    }
}
