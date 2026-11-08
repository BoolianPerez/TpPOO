package com.tppoo.Vista;

import com.tppoo.Controlador.ControladorNotificaciones;
import com.tppoo.Controlador.ControladorNotificaciones.TipoCanal;
import com.tppoo.Modelo.Notificacion.CanalNotificacion;
import com.tppoo.Modelo.Notificacion.Suscriptor;
import com.tppoo.Modelo.Club.Club;

import java.util.ArrayList;
import java.util.List;

public class VistaNotificaciones {
    private final Consola consola;
    private final ControladorNotificaciones controlador;

    public VistaNotificaciones(Consola consola, ControladorNotificaciones controlador) {
        this.consola = consola;
        this.controlador = controlador;
    }

    public void ejecutar() {
        int opcion;
        do {
            opcion = consola.menu("Notificaciones",
                    "Alta de suscriptor", "Seguir un club", "Dejar de seguir un club",
                    "Habilitar canal", "Quitar canal", "Eliminar suscriptor",
                    "Listar suscriptores", "Ver envíos y fallos");
            switch (opcion) {
                case 1 -> consola.intentar(this::alta);
                case 2 -> consola.intentar(this::seguir);
                case 3 -> consola.intentar(this::dejarDeSeguir);
                case 4 -> consola.intentar(this::habilitarCanal);
                case 5 -> consola.intentar(this::quitarCanal);
                case 6 -> consola.intentar(this::eliminar);
                case 7 -> consola.listar("Suscriptores", controlador.listarSuscriptores(), VistaNotificaciones::describir);
                case 8 -> verEnvios();
                default -> { }
            }
        } while (opcion != 0);
    }

    private void alta() {
        Suscriptor suscriptor = controlador.crearSuscriptor(consola.leerTexto("Nombre"),
                consola.leerTextoOpcional("Email"), consola.leerTextoOpcional("Teléfono"),
                consola.leerTextoOpcional("Token de la app"));
        consola.mostrar("Suscriptor creado: " + describir(suscriptor));
    }

    private void seguir() {
        Suscriptor suscriptor = elegirSuscriptor();
        Club club = suscriptor == null ? null : consola.elegir("Club a seguir", controlador.listarClubes(), Club::getNombre);
        if (club != null) {
            controlador.seguirClub(suscriptor, club);
            consola.mostrar(suscriptor.getNombre() + " sigue a " + club.getNombre());
        }
    }

    private void dejarDeSeguir() {
        Suscriptor suscriptor = elegirSuscriptor();
        Club club = suscriptor == null ? null : consola.elegir("Club", suscriptor.getClubesQueSigue(), Club::getNombre);
        if (club != null) {
            controlador.dejarDeSeguirClub(suscriptor, club);
            consola.mostrar("Listo.");
        }
    }

    private void habilitarCanal() {
        Suscriptor suscriptor = elegirSuscriptor();
        TipoCanal tipo = suscriptor == null ? null : consola.elegirEnum("Canal", TipoCanal.class);
        if (tipo != null) {
            controlador.habilitarCanal(suscriptor, tipo);
            consola.mostrar("Canales: " + canales(suscriptor));
        }
    }

    private void quitarCanal() {
        Suscriptor suscriptor = elegirSuscriptor();
        TipoCanal tipo = suscriptor == null ? null : consola.elegirEnum("Canal a quitar", TipoCanal.class);
        if (tipo != null) {
            controlador.quitarCanal(suscriptor, tipo);
            consola.mostrar("Canales: " + canales(suscriptor));
        }
    }

    private void eliminar() {
        Suscriptor suscriptor = elegirSuscriptor();
        if (suscriptor != null && consola.confirmar("¿Eliminar a " + suscriptor.getNombre() + "?")) {
            controlador.quitarSuscriptor(suscriptor);
            consola.mostrar("Suscriptor eliminado.");
        }
    }

    private void verEnvios() {
        consola.titulo("Envíos");
        consola.mostrar("Envíos intentados: " + controlador.getEnviosIntentados());
        consola.listar("Fallos registrados", controlador.getRegistroFallos(), texto -> texto);
    }

    private Suscriptor elegirSuscriptor() {
        return consola.elegir("Suscriptor", controlador.listarSuscriptores(), VistaNotificaciones::describir);
    }

    private static String describir(Suscriptor suscriptor) {
        List<String> clubes = new ArrayList<>();
        for (Club club : suscriptor.getClubesQueSigue()) {
            clubes.add(club.getNombre());
        }
        return suscriptor.getNombre() + " - sigue: " + (clubes.isEmpty() ? "nadie" : String.join(", ", clubes))
                + " - canales: " + canales(suscriptor);
    }

    private static String canales(Suscriptor suscriptor) {
        List<String> nombres = new ArrayList<>();
        for (CanalNotificacion canal : suscriptor.getCanalesHabilitados()) {
            nombres.add(canal.getClass().getSimpleName().replace("Canal", ""));
        }
        return nombres.isEmpty() ? "ninguno" : String.join(", ", nombres);
    }
}
