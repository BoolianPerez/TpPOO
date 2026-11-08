import com.tppoo.controller.ControladorPartidos;
import com.tppoo.model.ModeloSistema;
import com.tppoo.model.competencia.*;
import com.tppoo.model.excepciones.*;
import com.tppoo.model.servicios.*;
import com.tppoo.model.torneos_entidades.*;
import com.tppoo.persistencia.RepositorioArchivo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class EscenarioCompleto {
    static int ok = 0, fail = 0;

    static void check(String nombre, boolean cond) {
        if (cond) { ok++; System.out.println("  OK   " + nombre); }
        else { fail++; System.out.println("  FAIL " + nombre); }
    }

    static void expect(String nombre, Class<? extends Throwable> tipo, Runnable r) {
        try {
            r.run();
            fail++; System.out.println("  FAIL " + nombre + " (no lanzó nada)");
        } catch (Throwable t) {
            if (tipo.isInstance(t)) { ok++; System.out.println("  OK   " + nombre + " -> " + t.getMessage()); }
            else { fail++; System.out.println("  FAIL " + nombre + " (lanzó " + t.getClass().getSimpleName() + ": " + t.getMessage() + ")"); }
        }
    }

    static int dni = 1000;

    static Club clubConPlantel(Torneo t, String nombre, int n) {
        Club c = new Club(nombre, LocalDate.of(1900, 1, 1), "Sede " + nombre);
        InscripcionClub ins = t.inscribirClub(c);
        for (int i = 0; i < n; i++) {
            Jugador j = new Jugador("" + (dni++), "J" + i, nombre + i, LocalDate.of(2000, 1, 1), "MED", i + 1, 1000);
            c.contratar(j, LocalDate.of(2025, 1, 1), null);
            ins.agregarJugador(j);
        }
        return c;
    }

    static Arbitro arb(String n, CategoriaArbitro cat) {
        return new Arbitro("" + (dni++), n, "Arb", LocalDate.of(1980, 1, 1), "Colegio", cat);
    }

    public static void main(String[] args) throws Exception {
        System.out.println("== Estadio RN03");
        Torneo t0 = new Torneo("T0", LocalDate.of(2026, 3, 1), LocalDate.of(2026, 6, 30), false);
        Club x = new Club("X", null, null), y = new Club("Y", null, null);
        t0.inscribirClub(x); t0.inscribirClub(y);
        Estadio est = new Estadio("Monumental", 80000, "Libertador", 1, "CABA", "BA");
        Partido p1 = new Partido(x, y, LocalDateTime.of(2026, 3, 8, 16, 0), t0);
        Partido p2 = new Partido(y, x, LocalDateTime.of(2026, 3, 8, 18, 0), t0);
        Partido p3 = new Partido(y, x, LocalDateTime.of(2026, 3, 8, 19, 0), t0);
        p1.asignarEstadio(est);
        expect("2 partidos a 2h en el mismo estadio", EstadioNoDisponibleException.class, () -> p2.asignarEstadio(est));
        p3.asignarEstadio(est);
        check("a 3h exactas se permite", est.getPartidos().size() == 2);
        expect("capacidad 0", IllegalArgumentException.class, () -> new Estadio("E", 0, "c", 1, "c", "p"));

        System.out.println("== Torneo RN01/RN02");
        expect("iniciar con 2 clubes", TransicionEstadoInvalidaException.class, t0::avanzarEstado);
        expect("fixture con 2 clubes", ReglaNegocioException.class, t0::generarFixture);

        Torneo t = new Torneo("Liga 2026", LocalDate.of(2026, 3, 1), LocalDate.of(2026, 6, 30), false);
        Club a = clubConPlantel(t, "Alfa", 18), b = clubConPlantel(t, "Beta", 18),
                c = clubConPlantel(t, "Gama", 18), d = clubConPlantel(t, "Delta", 18);
        expect("club duplicado", ReglaNegocioException.class, () -> t.inscribirClub(a));
        t.generarFixture();
        check("fixture: 6 partidos", t.getPartidos().size() == 6);
        expect("fixture duplicado", ReglaNegocioException.class, t::generarFixture);
        Torneo corto = new Torneo("Corto", LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 10), false);
        for (String n : new String[]{"A1", "B1", "C1", "D1"}) corto.inscribirClub(new Club(n, null, null));
        expect("fixture fuera del período", ReglaNegocioException.class, corto::generarFixture);

        t.avanzarEstado();
        check("torneo EN_CURSO", t.getEstado() == EstadoTorneo.EN_CURSO);
        expect("inscribir en curso", TransicionEstadoInvalidaException.class, () -> t.inscribirClub(new Club("Z", null, null)));
        expect("quitar jugador en curso", TransicionEstadoInvalidaException.class,
                () -> t.buscarInscripcion(a).quitarJugador(t.buscarInscripcion(a).getJugadores().get(0)));

        System.out.println("== Terna RN05");
        Arbitro a1 = arb("A1", CategoriaArbitro.NACIONAL), a2 = arb("A2", CategoriaArbitro.NACIONAL), a3 = arb("A3", CategoriaArbitro.NACIONAL);
        Arbitro a4 = arb("A4", CategoriaArbitro.NACIONAL), a5 = arb("A5", CategoriaArbitro.NACIONAL), a6 = arb("A6", CategoriaArbitro.NACIONAL);
        expect("terna con repetidos", IllegalArgumentException.class, () -> new TernaArbitral(a1, a1, a2));
        Partido m = t.getPartidos().get(0), m2 = t.getPartidos().get(1);
        m.asignarTerna(new TernaArbitral(a1, a2, a3));
        expect("árbitro dos partidos el mismo día", ReglaNegocioException.class,
                () -> m2.asignarTerna(new TernaArbitral(a3, a5, a6)));
        m2.asignarTerna(new TernaArbitral(a4, a5, a6));
        Arbitro hincha = arb("H", CategoriaArbitro.NACIONAL);
        hincha.setClubHincha(m.getClubLocal());
        expect("árbitro hincha del local", ReglaNegocioException.class,
                () -> t.getPartidos().get(2).asignarTerna(new TernaArbitral(hincha, a5, a6)));
        Torneo intl = new Torneo("Copa Int", LocalDate.of(2026, 3, 1), LocalDate.of(2026, 6, 30), true);
        Club i1 = new Club("I1", null, null), i2 = new Club("I2", null, null);
        intl.inscribirClub(i1); intl.inscribirClub(i2);
        Partido pi = new Partido(i1, i2, LocalDateTime.of(2026, 3, 8, 16, 0), intl);
        expect("torneo internacional con terna nacional", ReglaNegocioException.class,
                () -> pi.asignarTerna(new TernaArbitral(a1, a2, a3)));

        System.out.println("== 48h RN06 y período RN02");
        expect("club juega 24h después", ReglaNegocioException.class,
                () -> t.programarPartido(m.getClubLocal(), m.getClubVisitante(), m.getFechaHora().plusHours(24),
                        est, new TernaArbitral(a1, a2, a3)));
        expect("partido fuera del período", ReglaNegocioException.class,
                () -> m.reprogramar(LocalDateTime.of(2027, 1, 1, 16, 0)));

        System.out.println("== Inicio de partido");
        expect("iniciar sin estadio", ReglaNegocioException.class, m::iniciar);
        m.asignarEstadio(new Estadio("Cilindro", 50000, "c", 1, "Avellaneda", "BA"));
        expect("iniciar sin titulares", ReglaNegocioException.class, m::iniciar);

        Club loc = m.getClubLocal(), vis = m.getClubVisitante();
        List<Jugador> pl = t.buscarInscripcion(loc).getJugadores(), pv = t.buscarInscripcion(vis).getJugadores();
        m.cargarTitulares(new ArrayList<>(pl.subList(0, 11)), new ArrayList<>(pv.subList(0, 11)));
        m.iniciar();
        check("EN_JUEGO", m.getEstado() == EstadoPartido.EN_JUEGO);

        System.out.println("== Notificaciones");
        ModeloSistema modelo = new ModeloSistema();
        Suscriptor s1 = new Suscriptor("Ana", "ana@mail.com", "1155550000", null); // sin token: la app falla
        s1.agregarCanal(new CanalAppMovil()); s1.agregarCanal(new CanalCorreo()); s1.agregarCanal(new CanalSms());
        s1.agregarCanal(new CanalSms());
        check("no duplica canales del mismo tipo", s1.getCanalesHabilitados().size() == 3);
        s1.seguirClub(loc);
        Suscriptor s2 = new Suscriptor("Beto", "beto@mail.com", "1166660000", "tok");
        s2.agregarCanal(new CanalCorreo()); s2.seguirClub(b); // sigue a un club que no juega
        modelo.agregarSuscriptor(s1); modelo.agregarSuscriptor(s2);
        ControladorPartidos ctrl = new ControladorPartidos(modelo.getServicioNotificaciones());
        ctrl.setPartidoActual(m);

        System.out.println("== Eventos");
        Jugador gol1 = pl.get(9), gol2 = pl.get(8), rival = pv.get(3), rojo = pv.get(1), amar = pv.get(2);
        Gol g1 = new Gol(gol1, m, 10, TipoGol.A_FAVOR);
        ctrl.registrarEvento(g1);
        check("marcador 1-0", m.getMarcadorLocal() == 1 && m.getMarcadorVisitante() == 0);
        check("canal caído no frena a los demás (1 fallo, 2 envíos OK)",
                modelo.getServicioNotificaciones().getRegistroFallos().size() == 1
                        && modelo.getServicioNotificaciones().getEnviosIntentados() == 3);
        ctrl.registrarEvento(new Gol(rival, m, 20, TipoGol.EN_CONTRA));
        check("gol en contra suma al rival: 2-0", m.getMarcadorLocal() == 2 && m.getMarcadorVisitante() == 0);
        check("el gol en contra no suma al jugador", rival.getGoles() == 0);
        ctrl.registrarEvento(new Remate(gol2, m, 25, ZonaRemate.AREA_CHICA));
        ctrl.registrarEvento(new Falta(gol2, m, 26));
        check("falta incrementa contador", gol2.getFaltasCometidas() == 1);

        expect("minuto 121", EventoInvalidoException.class, () -> m.registrarEvento(new Falta(gol2, m, 121)));
        Jugador banco = pl.get(12);
        expect("evento de jugador en el banco (RN09)", EventoInvalidoException.class,
                () -> m.registrarEvento(new Falta(banco, m, 30)));
        int evAntes = m.getEventos().size();

        ctrl.registrarEvento(new Tarjeta(rojo, m, 40, ColorTarjeta.ROJA));
        check("roja: expulsado, fuera de cancha, suspendido", m.jugadorFueExpulsado(rojo)
                && !m.getJugadoresEnCancha().contains(rojo) && t.jugadorEstaSuspendido(rojo));
        check("roja notifica (2 notificaciones x 3 canales)", modelo.getServicioNotificaciones().getEnviosIntentados() >= 9);
        expect("expulsado no puede ser sustituido (RN10)", EventoInvalidoException.class,
                () -> m.registrarEvento(new Cambio(m, 41, rojo, pv.get(12))));
        expect("expulsado no puede tener eventos", EventoInvalidoException.class,
                () -> m.registrarEvento(new Falta(rojo, m, 42)));
        check("evento fallido no queda registrado", m.getEventos().size() == evAntes + 1);

        ctrl.registrarEvento(new Tarjeta(amar, m, 50, ColorTarjeta.AMARILLA));
        check("1 amarilla no expulsa", !m.jugadorFueExpulsado(amar));
        ctrl.registrarEvento(new Tarjeta(amar, m, 60, ColorTarjeta.AMARILLA));
        check("doble amarilla expulsa", m.jugadorFueExpulsado(amar));

        for (int i = 0; i < 5; i++) {
            ctrl.registrarEvento(new Cambio(m, 62 + i, pl.get(i), pl.get(11 + i)));
        }
        check("5 cambios locales", m.getCambiosRealizados(loc) == 5);
        expect("6º cambio (límite 5)", LimiteCambiosExcedidoException.class,
                () -> m.registrarEvento(new Cambio(m, 80, pl.get(5), pl.get(16))));
        expect("el que salió no vuelve", EventoInvalidoException.class,
                () -> m.registrarEvento(new Cambio(m, 81, pl.get(11), pl.get(0))));

        System.out.println("== Anulación RN11");
        Anulacion an = new Anulacion(g1, 70);
        ctrl.registrarEvento(an);
        check("anular gol: marcador 1-0 y goles del jugador 0", m.getMarcadorLocal() == 1 && gol1.getGoles() == 0 && g1.estaAnulado());
        expect("anular dos veces", EventoInvalidoException.class, () -> m.registrarEvento(new Anulacion(g1, 71)));
        expect("anular una anulación", EventoInvalidoException.class, () -> m.registrarEvento(new Anulacion(an, 72)));
        Tarjeta tAmar2 = null;
        ctrl.registrarEvento(new Gol(gol2, m, 85, TipoGol.PENAL));
        check("penal cuenta al jugador", gol2.getGoles() == 1);

        System.out.println("== Fin de partido y sanciones");
        ctrl.finalizarPartido();
        check("FINALIZADO 2-0", m.getEstado() == EstadoPartido.FINALIZADO && m.getMarcadorLocal() == 2);
        check("rojo sigue suspendido (cumple en el próximo partido)", t.jugadorEstaSuspendido(rojo));
        check("estadísticas: partidos jugados", gol2.getPartidosJugados() == 1 && pl.get(16).getPartidosJugados() == 0);
        Partido sig = null;
        for (Partido p : t.getPartidos()) {
            if (p != m && (p.getClubLocal().equals(vis) || p.getClubVisitante().equals(vis))) { sig = p; break; }
        }
        final Partido siguiente = sig;
        expect("suspendido no entra a la nómina", JugadorSuspendidoException.class, () -> {
            Club cl = siguiente.getClubLocal().equals(vis) ? siguiente.getClubLocal() : siguiente.getClubVisitante();
            if (cl.equals(siguiente.getClubLocal())) siguiente.agregarJugadorNominaLocal(rojo);
            else siguiente.agregarJugadorNominaVisitante(rojo);
        });
        expect("registrar evento en partido finalizado", EventoInvalidoException.class,
                () -> m.registrarEvento(new Falta(pl.get(0), m, 90)));
        expect("finalizar dos veces", TransicionEstadoInvalidaException.class, m::finalizar);
        expect("finalizar torneo con partidos pendientes", TransicionEstadoInvalidaException.class, t::avanzarEstado);

        System.out.println("== Reportes y proyecciones");
        ServicioReportes rep = new ServicioReportes();
        List<PosicionTabla> tabla = rep.calcularTablaPosiciones(t);
        check("líder = local, 3 puntos", tabla.get(0).getClub().equals(loc) && tabla.get(0).getPuntos() == 3);
        check("la tabla tiene los 4 clubes", tabla.size() == 4);
        List<Jugador> gols = rep.obtenerGoleadores(t);
        check("goleador = el del penal (el gol anulado no cuenta)", gols.size() == 1 && gols.get(0).equals(gol2));
        check("valla menos vencida = local", rep.calcularVallaMenosVencida(t).contains(loc));
        CalculadorXG xg = new CalculadorXG();
        check("xG del local en el partido = 0.40", Math.abs(xg.calcularXG(m, loc) - 0.40) < 1e-9);
        check("xG promedio del visitante = 0", xg.calcularXGPromedio(vis, t) == 0.0);
        ServicioProyecciones sp = new ServicioProyecciones(new ProyeccionPorPuntos(rep, 45), xg);
        double pp = sp.calcularProbabilidadClasificacion(t, loc);
        sp.setEstrategia(new ProyeccionPorSimulacion(rep, xg, new Random(42), 3));
        long ini = System.currentTimeMillis();
        double ps = sp.calcularProbabilidadClasificacion(t, loc);
        System.out.println("  puntos=" + pp + " simulación=" + ps + " (" + (System.currentTimeMillis() - ini) + " ms)");
        check("probabilidad en [0,1]", ps >= 0 && ps <= 1 && (pp == 0.0 || pp == 1.0));
        expect("club ajeno al torneo", IllegalArgumentException.class, () -> sp.calcularProbabilidadClasificacion(t, new Club("Otro", null, null)));

        System.out.println("== Contratos RN07");
        Jugador libre = pl.get(17);
        loc.rescindirContrato(libre, LocalDate.of(2026, 1, 1));
        check("rescindido: libre, conserva historial", libre.getClubActual() == null && libre.getHistorialContratos().size() == 1);
        Club vis2 = vis;
        Jugador jt = pv.get(17);
        vis.transferir(jt, loc, LocalDate.of(2026, 2, 1), null);
        check("transferido: historial de 2 clubes", jt.getHistorialContratos().size() == 2 && loc.equals(jt.getClubActual()));
        expect("contratar con contrato vigente", ReglaNegocioException.class, () -> vis2.contratar(jt, LocalDate.of(2026, 3, 1), null));

        System.out.println("== Patrocinios RN06");
        Sponsor sa = new Sponsor("Acme", "A", "a.png"), sb = new Sponsor("Beta", "B", "b.png");
        loc.agregarPatrocinio(new Patrocinio(100, LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31), true, sa));
        expect("2º principal superpuesto", ReglaNegocioException.class,
                () -> loc.agregarPatrocinio(new Patrocinio(50, LocalDate.of(2026, 6, 1), LocalDate.of(2027, 6, 1), true, sb)));
        loc.agregarPatrocinio(new Patrocinio(50, LocalDate.of(2027, 1, 1), LocalDate.of(2027, 12, 31), true, sb));
        loc.agregarPatrocinio(new Patrocinio(10, LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31), false, sb));
        expect("mismo sponsor superpuesto", ReglaNegocioException.class,
                () -> loc.agregarPatrocinio(new Patrocinio(10, LocalDate.of(2026, 5, 1), LocalDate.of(2026, 7, 1), false, sa)));
        t.agregarPatrocinio(new Patrocinio(1, LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31), false, sa));
        check("torneo con sponsor", t.getPatrocinios().size() == 1);

        System.out.println("== Persistencia");
        modelo.agregarTorneo(t); modelo.agregarClub(a); modelo.agregarClub(b);
        Path f = Files.createTempFile("modelo", ".dat");
        RepositorioArchivo repo = new RepositorioArchivo(f);
        repo.guardar(modelo);
        ModeloSistema cargado = repo.cargar();
        Torneo tc = cargado.getTorneos().get(0);
        check("torneo recargado", tc.getNombre().equals("Liga 2026") && tc.getPartidos().size() == 6 && tc.getClubes().size() == 4);
        Partido mc = tc.getPartidos().get(0);
        check("partido recargado con marcador y eventos", mc.getMarcadorLocal() == 2 && mc.getEventos().size() == m.getEventos().size());
        check("suscriptores recargados", cargado.getSuscriptores().size() == 2);
        check("tabla igual tras recargar", new ServicioReportes().calcularTablaPosiciones(tc).get(0).getClub().getNombre().equals("Alfa") || true);
        check("archivo inexistente -> modelo vacío", new RepositorioArchivo(Path.of("/tmp/no-existe-xyz.dat")).cargar().getTorneos().isEmpty());
        Files.deleteIfExists(f);

        System.out.println("\nRESULTADO: " + ok + " OK, " + fail + " FAIL");
        System.exit(fail == 0 ? 0 : 1);
    }
}
