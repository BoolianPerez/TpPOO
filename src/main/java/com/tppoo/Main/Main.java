package com.tppoo.Main;

import com.tppoo.Controlador.ControladorSistema;
import com.tppoo.persistencia.RepositorioArchivo;
import com.tppoo.Vista.Consola;

import java.nio.file.Path;

public class Main {
    private static final String ARCHIVO_POR_DEFECTO = "futbolstats.dat";

    public static void main(String[] args) {
        Path archivo = Path.of(args.length > 0 ? args[0] : ARCHIVO_POR_DEFECTO);
        Consola consola = new Consola(System.in, System.out);
        new ControladorSistema(new RepositorioArchivo(archivo), consola).iniciar();
    }
}
