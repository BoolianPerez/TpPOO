package com.tppoo.Controlador;

import com.tppoo.Modelo.ModeloSistema;
import com.tppoo.persistencia.PersistenciaException;
import com.tppoo.persistencia.RepositorioArchivo;
import com.tppoo.Vista.Consola;
import com.tppoo.Vista.VistaClubes;
import com.tppoo.Vista.VistaNotificaciones;
import com.tppoo.Vista.VistaPartidos;
import com.tppoo.Vista.VistaPersonas;
import com.tppoo.Vista.VistaPrincipal;
import com.tppoo.Vista.VistaReportes;
import com.tppoo.Vista.VistaTorneos;

import java.util.NoSuchElementException;


public class ControladorSistema {
    private final RepositorioArchivo repositorio;
    private final Consola consola;
    private ModeloSistema modelo;

    public ControladorSistema(RepositorioArchivo repositorio, Consola consola) {
        this.repositorio = repositorio;
        this.consola = consola;
    }

    public void iniciar() {
        modelo = cargarModelo();
        VistaPrincipal vista = new VistaPrincipal(consola, this,
                new VistaClubes(consola, new ControladorClubes(modelo)),
                new VistaPersonas(consola, new ControladorPersonas(modelo)),
                new VistaTorneos(consola, new ControladorTorneos(modelo)),
                new VistaPartidos(consola, new ControladorPartidos(modelo)),
                new VistaNotificaciones(consola, new ControladorNotificaciones(modelo)),
                new VistaReportes(consola, new ControladorReportes(modelo)));
        try {
            vista.ejecutar();
        } catch (NoSuchElementException e) {
            consola.mostrar("\nSe cerró la entrada de datos.");
        } finally {
            guardarAlSalir();
        }
    }

    public void guardar() throws PersistenciaException {
        repositorio.guardar(modelo);
    }

    private ModeloSistema cargarModelo() {
        try {
            return repositorio.cargar();
        } catch (PersistenciaException e) {
            consola.error("No se pudieron cargar los datos guardados (" + e.getMessage() + "). Se inicia vacío.");
            return new ModeloSistema();
        }
    }

    private void guardarAlSalir() {
        try {
            guardar();
            consola.mostrar("Datos guardados.");
        } catch (PersistenciaException e) {
            consola.error("No se pudieron guardar los datos: " + e.getMessage());
        }
    }
}
