package com.tppoo.Controlador;

import com.tppoo.Modelo.ModeloSistema;
import com.tppoo.Modelo.Notificacion.CanalAppMovil;
import com.tppoo.Modelo.Notificacion.CanalCorreo;
import com.tppoo.Modelo.Notificacion.CanalNotificacion;
import com.tppoo.Modelo.Notificacion.CanalSms;
import com.tppoo.Modelo.Notificacion.ServicioNotificaciones;
import com.tppoo.Modelo.Notificacion.Suscriptor;
import com.tppoo.Modelo.Club.Club;

import java.util.List;

public class ControladorNotificaciones {
    public enum TipoCanal {
        APP_MOVIL, CORREO, SMS;

        CanalNotificacion crear() {
            switch (this) {
                case APP_MOVIL:
                    return new CanalAppMovil();
                case CORREO:
                    return new CanalCorreo();
                default:
                    return new CanalSms();
            }
        }
    }

    private final ModeloSistema modelo;
    private final ServicioNotificaciones servicio;

    public ControladorNotificaciones(ModeloSistema modelo) {
        this.modelo = modelo;
        this.servicio = modelo.getServicioNotificaciones();
    }

    public Suscriptor crearSuscriptor(String nombre, String email, String telefono, String tokenApp) {
        Suscriptor suscriptor = new Suscriptor(nombre, vacioANull(email), vacioANull(telefono), vacioANull(tokenApp));
        modelo.agregarSuscriptor(suscriptor);
        return suscriptor;
    }

    public void seguirClub(Suscriptor suscriptor, Club club) {
        suscriptor.seguirClub(club);
    }

    public void dejarDeSeguirClub(Suscriptor suscriptor, Club club) {
        suscriptor.dejarDeSeguirClub(club);
    }

    public void habilitarCanal(Suscriptor suscriptor, TipoCanal tipo) {
        suscriptor.agregarCanal(tipo.crear());
    }

    public void quitarCanal(Suscriptor suscriptor, TipoCanal tipo) {
        suscriptor.removerCanal(tipo.crear());
    }

    public void quitarSuscriptor(Suscriptor suscriptor) {
        servicio.quitarSuscriptor(suscriptor);
    }

    public List<Suscriptor> listarSuscriptores() {
        return modelo.getSuscriptores();
    }

    public List<Club> listarClubes() {
        return modelo.getClubes();
    }

    public List<String> getRegistroFallos() {
        return servicio.getRegistroFallos();
    }

    public int getEnviosIntentados() {
        return servicio.getEnviosIntentados();
    }

    private String vacioANull(String texto) {
        return texto == null || texto.isBlank() ? null : texto;
    }
}
