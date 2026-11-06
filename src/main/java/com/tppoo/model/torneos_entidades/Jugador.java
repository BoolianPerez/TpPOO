package com.tppoo.model.torneos_entidades;

import java.time.LocalDate;

public class Jugador extends Persona {
    private String posicion;
    private int numeroCamiseta;
    private double valorMercado;
    private int partidosJugados;
    private int goles;
    private int tarjetas;

    public Jugador() {
    }

    public Jugador(String dni, String nombre, String apellido, LocalDate fechaNacimiento,
                   String posicion, int numeroCamiseta, double valorMercado) {
        super(dni, nombre, apellido, fechaNacimiento);
        this.posicion = posicion;
        this.numeroCamiseta = numeroCamiseta;
        this.valorMercado = valorMercado;
    }

    @Override
    public String obtenerDetalle() {
        return "Jugador: " + super.obtenerDetalle();
    }

    public void registrarPagoPorClub(Contrato contrato) {
        // TODO: registrar pago asociado al contrato del jugador.
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    public int getNumeroCamiseta() {
        return numeroCamiseta;
    }

    public void setNumeroCamiseta(int numeroCamiseta) {
        this.numeroCamiseta = numeroCamiseta;
    }

    public double getValorMercado() {
        return valorMercado;
    }

    public void setValorMercado(double valorMercado) {
        this.valorMercado = valorMercado;
    }

    public int getPartidosJugados() {
        return partidosJugados;
    }

    public void setPartidosJugados(int partidosJugados) {
        this.partidosJugados = partidosJugados;
    }

    public int getGoles() {
        return goles;
    }

    public void setGoles(int goles) {
        this.goles = goles;
    }

    public int getTarjetas() {
        return tarjetas;
    }

    public void setTarjetas(int tarjetas) {
        this.tarjetas = tarjetas;
    }
}