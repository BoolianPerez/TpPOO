package com.tppoo.controller;

import com.tppoo.model.ModeloSistema;
import com.tppoo.view.VistaPrincipal;

public class ControladorSistema {
    private final ModeloSistema modelo;
    private final VistaPrincipal vista;

    public ControladorSistema(ModeloSistema modelo, VistaPrincipal vista) {
        this.modelo = modelo;
        this.vista = vista;
    }

    public void iniciar() {
        vista.mostrarMensaje("Sistema inicializado");
    }

    public ModeloSistema getModelo() {
        return modelo;
    }

    public VistaPrincipal getVista() {
        return vista;
    }
}