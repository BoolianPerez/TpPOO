package com.tppoo.Vista;

import com.tppoo.Controlador.ControladorSistema;
import com.tppoo.persistencia.PersistenciaException;

public class VistaPrincipal {
    private final Consola consola;
    private final ControladorSistema controlador;
    private final VistaClubes vistaClubes;
    private final VistaPersonas vistaPersonas;
    private final VistaTorneos vistaTorneos;
    private final VistaPartidos vistaPartidos;
    private final VistaNotificaciones vistaNotificaciones;
    private final VistaReportes vistaReportes;

    public VistaPrincipal(Consola consola, ControladorSistema controlador, VistaClubes vistaClubes,
                          VistaPersonas vistaPersonas, VistaTorneos vistaTorneos, VistaPartidos vistaPartidos,
                          VistaNotificaciones vistaNotificaciones, VistaReportes vistaReportes) {
        this.consola = consola;
        this.controlador = controlador;
        this.vistaClubes = vistaClubes;
        this.vistaPersonas = vistaPersonas;
        this.vistaTorneos = vistaTorneos;
        this.vistaPartidos = vistaPartidos;
        this.vistaNotificaciones = vistaNotificaciones;
        this.vistaReportes = vistaReportes;
    }

    public void ejecutar() {
        consola.mostrar("FutbolStats - gestión de torneos de fútbol");
        int opcion;
        do {
            opcion = consola.menu("Menú principal", "Clubes, estadios y sponsors", "Personas (jugadores, árbitros, entrenadores)",
                    "Torneos", "Partidos", "Notificaciones", "Reportes y proyecciones", "Guardar ahora");
            switch (opcion) {
                case 1 -> vistaClubes.ejecutar();
                case 2 -> vistaPersonas.ejecutar();
                case 3 -> vistaTorneos.ejecutar();
                case 4 -> vistaPartidos.ejecutar();
                case 5 -> vistaNotificaciones.ejecutar();
                case 6 -> vistaReportes.ejecutar();
                case 7 -> guardar();
                default -> { }
            }
        } while (opcion != 0);
    }

    private void guardar() {
        try {
            controlador.guardar();
            consola.mostrar("Datos guardados.");
        } catch (PersistenciaException e) {
            consola.error("No se pudo guardar: " + e.getMessage());
        }
    }
}
