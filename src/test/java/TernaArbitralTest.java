import com.tppoo.Modelo.Excepcion.ReglaNegocioException;
import com.tppoo.Modelo.Persona.Arbitro;
import com.tppoo.Modelo.Persona.CategoriaArbitro;
import com.tppoo.Modelo.Torneo.Torneo;
import com.tppoo.Modelo.Partido.Partido;
import com.tppoo.Modelo.Partido.TernaArbitral;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TernaArbitralTest {

    @Test
    void noPermiteArbitroRepetidoEnLaTerna() {
        Arbitro a1 = DatosPrueba.arbitro("A1", CategoriaArbitro.NACIONAL);
        Arbitro a2 = DatosPrueba.arbitro("A2", CategoriaArbitro.NACIONAL);

        assertThrows(IllegalArgumentException.class,
                () -> new TernaArbitral(a1, a1, a2));
    }

    @Test
    void noPermiteArbitroEnDosPartidosElMismoDia() {
        Torneo torneo = DatosPrueba.torneoConCuatroClubes();
        torneo.generarFixture();
        Partido primero = torneo.getPartidos().get(0);
        Partido segundo = torneo.getPartidos().get(1);

        Arbitro a1 = DatosPrueba.arbitro("A1", CategoriaArbitro.NACIONAL);
        Arbitro a2 = DatosPrueba.arbitro("A2", CategoriaArbitro.NACIONAL);
        Arbitro a3 = DatosPrueba.arbitro("A3", CategoriaArbitro.NACIONAL);
        Arbitro a4 = DatosPrueba.arbitro("A4", CategoriaArbitro.NACIONAL);
        Arbitro a5 = DatosPrueba.arbitro("A5", CategoriaArbitro.NACIONAL);

        primero.asignarTerna(new TernaArbitral(a1, a2, a3));

        assertThrows(ReglaNegocioException.class,
                () -> segundo.asignarTerna(new TernaArbitral(a3, a4, a5)));
    }

    @Test
    void noPermiteArbitroHinchaDeUnClubParticipante() {
        Torneo torneo = DatosPrueba.torneoConCuatroClubes();
        torneo.generarFixture();
        Partido partido = torneo.getPartidos().get(0);

        Arbitro hincha = DatosPrueba.arbitro("Hincha", CategoriaArbitro.NACIONAL);
        Arbitro a2 = DatosPrueba.arbitro("A2", CategoriaArbitro.NACIONAL);
        Arbitro a3 = DatosPrueba.arbitro("A3", CategoriaArbitro.NACIONAL);
        hincha.setClubHincha(partido.getClubLocal());

        assertThrows(ReglaNegocioException.class,
                () -> partido.asignarTerna(new TernaArbitral(hincha, a2, a3)));
    }
}
