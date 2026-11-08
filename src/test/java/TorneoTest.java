import com.tppoo.Modelo.Excepcion.ReglaNegocioException;
import com.tppoo.Modelo.Excepcion.TransicionEstadoInvalidaException;
import com.tppoo.Modelo.Club.Club;
import com.tppoo.Modelo.Torneo.EstadoTorneo;
import com.tppoo.Modelo.Torneo.Torneo;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class TorneoTest {

    @Test
    void noPermiteIniciarConMenosDeCuatroClubes() {
        Torneo torneo = new Torneo("T", LocalDate.of(2026, 3, 1),
                LocalDate.of(2026, 6, 30), false);
        torneo.inscribirClub(new Club("A", null, null));
        torneo.inscribirClub(new Club("B", null, null));

        assertThrows(TransicionEstadoInvalidaException.class, torneo::avanzarEstado);
    }

    @Test
    void generaFixtureDeSeisPartidosParaCuatroClubes() {
        Torneo torneo = DatosPrueba.torneoConCuatroClubes();

        torneo.generarFixture();

        assertEquals(6, torneo.getPartidos().size());
    }

    @Test
    void noPermiteGenerarFixtureDosVeces() {
        Torneo torneo = DatosPrueba.torneoConCuatroClubes();
        torneo.generarFixture();

        assertThrows(ReglaNegocioException.class, torneo::generarFixture);
    }

    @Test
    void noPermiteInscribirClubDuplicado() {
        Torneo torneo = new Torneo("T", LocalDate.of(2026, 3, 1),
                LocalDate.of(2026, 6, 30), false);
        Club club = new Club("A", null, null);
        torneo.inscribirClub(club);

        assertThrows(ReglaNegocioException.class, () -> torneo.inscribirClub(club));
    }

    @Test
    void noPermiteFixtureFueraDelPeriodo() {
        Torneo torneo = new Torneo("Corto", LocalDate.of(2026, 3, 1),
                LocalDate.of(2026, 3, 10), false);
        for (String nombre : new String[]{"A", "B", "C", "D"}) {
            torneo.inscribirClub(new Club(nombre, null, null));
        }

        assertThrows(ReglaNegocioException.class, torneo::generarFixture);
    }

    @Test
    void avanzaAEnCursoCuandoCumpleLasCondiciones() {
        Torneo torneo = DatosPrueba.torneoConCuatroClubes();
        torneo.generarFixture();

        torneo.avanzarEstado();

        assertEquals(EstadoTorneo.EN_CURSO, torneo.getEstado());
    }
}
