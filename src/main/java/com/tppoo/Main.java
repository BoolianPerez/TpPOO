package com.tppoo;

import com.tppoo.controller.ControladorSistema;
import com.tppoo.model.ModeloSistema;
import com.tppoo.view.VistaPrincipal;

public class Main {
    public static void main(String[] args) {
        ModeloSistema modelo = new ModeloSistema();
        VistaPrincipal vista = new VistaPrincipal();
        ControladorSistema controlador = new ControladorSistema(modelo, vista);
        controlador.iniciar();
    }
}