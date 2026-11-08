import com.tppoo.Modelo.Excepcion.EstadioNoDisponibleException;
import com.tppoo.Modelo.Club.Club;
import com.tppoo.Modelo.Club.Estadio;
import com.tppoo.Modelo.Partido.Partido;
import com.tppoo.Modelo.Torneo.Torneo;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class EstadioTest {

    @Test
    void noPermiteDosPartidosConMenosDeTresHorasDeDiferencia() {
        Torneo torneo = new Torneo("T", java.time.LocalDate.of(2026, 3, 1),
                java.time.LocalDate.of(2026, 6, 30), false);
        Club local = new Club("Local", null, null);
        Club visitante = new Club("Visitante", null, null);
        torneo.inscribirClub(local);
        torneo.inscribirClub(visitante);

        Estadio estadio = DatosPrueba.estadio("Monumental");
        Partido primero = new Partido(local, visitante,
                LocalDateTime.of(2026, 3, 8, 16, 0), torneo);
        Partido segundo = new Partido(visitante, local,
                LocalDateTime.of(2026, 3, 8, 18, 0), torneo);

        primero.asignarEstadio(estadio);

        assertThrows(EstadioNoDisponibleException.class,
                () -> segundo.asignarEstadio(estadio));
    }

    @Test
    void permitePartidosSeparadosPorTresHorasExactas() {
        Torneo torneo = new Torneo("T", java.time.LocalDate.of(2026, 3, 1),
                java.time.LocalDate.of(2026, 6, 30), false);
        Club a = new Club("A", null, null);
        Club b = new Club("B", null, null);
        torneo.inscribirClub(a);
        torneo.inscribirClub(b);

        Estadio estadio = DatosPrueba.estadio("Estadio");
        Partido p1 = new Partido(a, b, LocalDateTime.of(2026, 3, 8, 16, 0), torneo);
        Partido p2 = new Partido(b, a, LocalDateTime.of(2026, 3, 8, 19, 0), torneo);

        p1.asignarEstadio(estadio);
        p2.asignarEstadio(estadio);

        assertEquals(2, estadio.getPartidos().size());
    }

    @Test
    void noPermiteEstadioSinCapacidad() {
        assertThrows(IllegalArgumentException.class,
                () -> new Estadio("E", 0, "Calle", 1, "Ciudad", "Provincia"));
    }
}
