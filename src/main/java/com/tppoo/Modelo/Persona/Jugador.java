package com.tppoo.Modelo.Persona;

import com.tppoo.Modelo.Club.Club;
import com.tppoo.Modelo.Club.Contrato;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Jugador extends Persona {
    private String posicion;
    private int numeroCamiseta;
    private double valorMercado;
    private int partidosJugados;
    private int goles;
    private int tarjetas;
    private int faltasCometidas;
    private final List<Contrato> historialContratos = new ArrayList<>();

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

    public void registrarPasoPorClub(Contrato contrato) {
        if (contrato != null && !historialContratos.contains(contrato)) {
            historialContratos.add(contrato);
        }
    }

    public Contrato getContratoVigenteEn(LocalDate fecha) {
        for (Contrato contrato : historialContratos) {
            if (contrato.esVigenteEn(fecha)) {
                return contrato;
            }
        }
        return null;
    }

    public Club getClubActual() {
        Contrato contrato = getContratoVigenteEn(LocalDate.now());
        return contrato == null ? null : contrato.getClub();
    }

    public List<Contrato> getHistorialContratos() {
        return Collections.unmodifiableList(historialContratos);
    }

    public void incrementarPartidosJugados() {
        this.partidosJugados++;
    }

    public void registrarTarjeta() {
        this.tarjetas++;
    }

    public void quitarTarjeta() {
        if (tarjetas > 0) {
            tarjetas--;
        }
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

    public int getGoles() {
        return goles;
    }

    public void setGoles(int goles) {
        this.goles = goles;
    }

    public int getTarjetas() {
        return tarjetas;
    }

    public int getFaltasCometidas() {
        return faltasCometidas;
    }

    public void incrementarFaltasCometidas() {
        this.faltasCometidas++;
    }

    public void decrementarFaltasCometidas() {
        if (faltasCometidas > 0) {
            faltasCometidas--;
        }
    }
}
