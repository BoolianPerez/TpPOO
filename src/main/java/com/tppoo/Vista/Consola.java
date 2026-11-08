package com.tppoo.Vista;

import java.io.InputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Function;


public class Consola {
    public static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final Scanner entrada;
    private final PrintStream salida;

    public Consola(InputStream entrada, PrintStream salida) {
        this.entrada = new Scanner(entrada);
        this.salida = salida;
    }


    public void mostrar(String texto) {
        salida.println(texto);
    }

    public void titulo(String texto) {
        salida.println();
        salida.println("=== " + texto + " ===");
    }

    public void error(String mensaje) {
        salida.println("[!] " + mensaje);
    }

    public <T> void listar(String titulo, List<T> elementos, Function<T, String> descripcion) {
        titulo(titulo);
        if (elementos.isEmpty()) {
            salida.println("(sin elementos)");
            return;
        }
        for (int i = 0; i < elementos.size(); i++) {
            salida.println((i + 1) + ") " + descripcion.apply(elementos.get(i)));
        }
    }


    private String leerLinea(String pregunta) {
        salida.print(pregunta + ": ");
        salida.flush();
        return entrada.nextLine().trim();
    }

    public String leerTexto(String pregunta) {
        while (true) {
            String texto = leerLinea(pregunta);
            if (!texto.isEmpty()) {
                return texto;
            }
            error("El dato es obligatorio.");
        }
    }

    public String leerTextoOpcional(String pregunta) {
        return leerLinea(pregunta + " (enter para omitir)");
    }

    public int leerEntero(String pregunta) {
        while (true) {
            try {
                return Integer.parseInt(leerLinea(pregunta));
            } catch (NumberFormatException e) {
                error("Ingresá un número entero.");
            }
        }
    }

    public double leerDecimal(String pregunta) {
        while (true) {
            try {
                return Double.parseDouble(leerLinea(pregunta).replace(',', '.'));
            } catch (NumberFormatException e) {
                error("Ingresá un número (ej: 1500000.50).");
            }
        }
    }

    public LocalDate leerFecha(String pregunta) {
        while (true) {
            try {
                return LocalDate.parse(leerLinea(pregunta + " [dd/MM/aaaa]"), FORMATO_FECHA);
            } catch (DateTimeParseException e) {
                error("Fecha inválida. Formato: dd/MM/aaaa.");
            }
        }
    }

    public LocalDateTime leerFechaHora(String pregunta) {
        while (true) {
            try {
                return LocalDateTime.parse(leerLinea(pregunta + " [dd/MM/aaaa HH:mm]"), FORMATO_FECHA_HORA);
            } catch (DateTimeParseException e) {
                error("Fecha y hora inválidas. Formato: dd/MM/aaaa HH:mm.");
            }
        }
    }

    public boolean confirmar(String pregunta) {
        while (true) {
            String respuesta = leerLinea(pregunta + " [s/n]").toLowerCase();
            if (respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("sí")) {
                return true;
            }
            if (respuesta.equals("n") || respuesta.equals("no")) {
                return false;
            }
            error("Respondé s o n.");
        }
    }


    public int menu(String titulo, String... opciones) {
        titulo(titulo);
        for (int i = 0; i < opciones.length; i++) {
            salida.println((i + 1) + ") " + opciones[i]);
        }
        salida.println("0) Volver");
        while (true) {
            int opcion = leerEntero("Opción");
            if (opcion >= 0 && opcion <= opciones.length) {
                return opcion;
            }
            error("Opción inexistente.");
        }
    }

    public <T> T elegir(String titulo, List<T> elementos, Function<T, String> descripcion) {
        if (elementos.isEmpty()) {
            error("No hay elementos para elegir (" + titulo + ").");
            return null;
        }
        listar(titulo, elementos, descripcion);
        salida.println("0) Cancelar");
        while (true) {
            int opcion = leerEntero("Elegí");
            if (opcion == 0) {
                return null;
            }
            if (opcion >= 1 && opcion <= elementos.size()) {
                return elementos.get(opcion - 1);
            }
            error("Opción inexistente.");
        }
    }

    public <E extends Enum<E>> E elegirEnum(String titulo, Class<E> tipo) {
        return elegir(titulo, List.of(tipo.getEnumConstants()), Enum::toString);
    }

    public void intentar(Runnable accion) {
        try {
            accion.run();
        } catch (NoSuchElementException e) {
            throw e;
        } catch (RuntimeException e) {
            error(e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName());
        }
    }
}
