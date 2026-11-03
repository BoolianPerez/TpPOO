package com.tppoo.view;

import java.util.List;

public class VistaPrincipal {
    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarListado(String titulo, List<String> elementos) {
        System.out.println(titulo);
        for (String elemento : elementos) {
            System.out.println("- " + elemento);
        }
    }

    public void mostrarDetalle(String detalle) {
        System.out.println(detalle);
    }
}