package com.tppoo.persistencia;

import com.tppoo.Modelo.ModeloSistema;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Guarda y carga todo el ModeloSistema en un archivo (serialización de Java),
 * para que los datos se conserven entre ejecuciones. Fabricación pura (GRASP).
 */
public class RepositorioArchivo {
    private final Path archivo;

    public RepositorioArchivo(Path archivo) {
        this.archivo = archivo;
    }

    public void guardar(ModeloSistema modelo) throws PersistenciaException {
        try (ObjectOutputStream salida = new ObjectOutputStream(Files.newOutputStream(archivo))) {
            salida.writeObject(modelo);
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo guardar el archivo " + archivo, e);
        }
    }

    /** Si el archivo todavía no existe (primera ejecución) devuelve un modelo vacío. */
    public ModeloSistema cargar() throws PersistenciaException {
        if (!Files.exists(archivo)) {
            return new ModeloSistema();
        }
        try (ObjectInputStream entrada = new ObjectInputStream(Files.newInputStream(archivo))) {
            return (ModeloSistema) entrada.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new PersistenciaException("No se pudo leer el archivo " + archivo, e);
        } finally {
            // los streams se cierran con try-with-resources; el finally queda para futuras tareas de limpieza
        }
    }
}
