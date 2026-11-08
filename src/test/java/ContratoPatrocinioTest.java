import com.tppoo.Modelo.Club.Club;
import com.tppoo.Modelo.Club.Patrocinio;
import com.tppoo.Modelo.Club.Sponsor;
import com.tppoo.Modelo.Excepcion.ReglaNegocioException;
import com.tppoo.Modelo.Persona.Jugador;
import com.tppoo.Modelo.Torneo.Torneo;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class ContratoPatrocinioTest {

    @Test
    void rescindirContratoDejaLibreAlJugadorYConservaHistorial() {
        Torneo torneo = new Torneo("T", LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 12, 31), false);
        Club club = new Club("Club", null, null);
        Jugador jugador = new Jugador("999", "Nombre", "Apellido",
                LocalDate.of(2000, 1, 1), "MED", 10, 1000);
        club.contratar(jugador, LocalDate.of(2025, 1, 1), null);

        club.rescindirContrato(jugador, LocalDate.of(2026, 1, 1));

        assertNull(jugador.getClubActual());
        assertEquals(1, jugador.getHistorialContratos().size());
    }

    @Test
    void noPermiteDosPatrociniosPrincipalesSuperpuestos() {
        Club club = new Club("Club", null, null);
        Sponsor primero = new Sponsor("Acme", "A", "a.png");
        Sponsor segundo = new Sponsor("Beta", "B", "b.png");
        club.agregarPatrocinio(new Patrocinio(100,
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31), true, primero));

        assertThrows(ReglaNegocioException.class, () ->
                club.agregarPatrocinio(new Patrocinio(50,
                        LocalDate.of(2026, 6, 1), LocalDate.of(2027, 6, 1), true, segundo)));
    }

    @Test
    void permitePatrociniosPrincipalesEnPeriodosNoSuperpuestos() {
        Club club = new Club("Club", null, null);
        Sponsor primero = new Sponsor("Acme", "A", "a.png");
        Sponsor segundo = new Sponsor("Beta", "B", "b.png");

        club.agregarPatrocinio(new Patrocinio(100,
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31), true, primero));
        club.agregarPatrocinio(new Patrocinio(50,
                LocalDate.of(2027, 1, 1), LocalDate.of(2027, 12, 31), true, segundo));

        assertEquals(2, club.getPatrocinios().size());
    }
}
