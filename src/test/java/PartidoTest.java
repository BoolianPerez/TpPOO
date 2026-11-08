import com.tppoo.Modelo.Excepcion.PlantelInvalidoException;
import com.tppoo.Modelo.Excepcion.ReglaNegocioException;
import com.tppoo.Modelo.Partido.EstadoPartido;
import com.tppoo.Modelo.Partido.Partido;
import com.tppoo.Modelo.Torneo.Torneo;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class PartidoTest {

    @Test
    void noPermiteIniciarSinEstadio() {
        Torneo torneo = DatosPrueba.torneoConCuatroClubes();
        torneo.generarFixture();
        torneo.avanzarEstado();
        Partido partido = torneo.getPartidos().get(0);
        partido.asignarTerna(DatosPrueba.ternaValida());

        assertThrows(ReglaNegocioException.class, partido::iniciar);
    }

    @Test
    void noPermiteIniciarSinTitulares() {
        Torneo torneo = DatosPrueba.torneoConCuatroClubes();
        torneo.generarFixture();
        torneo.avanzarEstado();
        Partido partido = torneo.getPartidos().get(0);
        partido.asignarTerna(DatosPrueba.ternaValida());
        partido.asignarEstadio(DatosPrueba.estadio("Estadio"));

        assertThrows(PlantelInvalidoException.class, partido::iniciar);
    }

    @Test
    void iniciaCuandoTieneEstadioTernaYOnceTitularesPorEquipo() {
        Torneo torneo = DatosPrueba.torneoConCuatroClubes();
        Partido partido = DatosPrueba.partidoEnJuego(torneo);

        assertNotNull(partido.getTerna());
        assertNotNull(partido.getEstadio());
        assertEquals(EstadoPartido.EN_JUEGO, partido.getEstado());
    }

    @Test
    void noPermiteReprogramarFueraDelPeriodoDelTorneo() {
        Torneo torneo = DatosPrueba.torneoConCuatroClubes();
        torneo.generarFixture();
        Partido partido = torneo.getPartidos().get(0);

        assertThrows(ReglaNegocioException.class,
                () -> partido.reprogramar(LocalDateTime.of(2027, 1, 1, 16, 0)));
    }
}
