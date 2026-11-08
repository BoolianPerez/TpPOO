package com.tppoo.Modelo.Partido;

import com.tppoo.Modelo.Persona.Arbitro;
import com.tppoo.Modelo.Persona.CategoriaArbitro;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TernaArbitral implements Serializable {
    private final Arbitro principal;
    private final Arbitro asistente1;
    private final Arbitro asistente2;

    public TernaArbitral(Arbitro principal, Arbitro asistente1, Arbitro asistente2) {
        if (principal == null || asistente1 == null || asistente2 == null) {
            throw new IllegalArgumentException("La terna arbitral requiere un principal y dos asistentes.");
        }
        if (mismoArbitro(principal, asistente1) || mismoArbitro(principal, asistente2) || mismoArbitro(asistente1, asistente2)) {
            throw new IllegalArgumentException("Los tres árbitros de la terna deben ser distintos.");
        }
        this.principal = principal;
        this.asistente1 = asistente1;
        this.asistente2 = asistente2;
    }

    public boolean contiene(Arbitro arbitro) {
        return mismoArbitro(principal, arbitro) || mismoArbitro(asistente1, arbitro) || mismoArbitro(asistente2, arbitro);
    }

    private static boolean mismoArbitro(Arbitro a, Arbitro b) {
        return a == b || a.mismoDni(b);
    }

    public boolean todosInternacionales() {
        for (Arbitro a : getArbitros()) {
            if (a.getCategoria() != CategoriaArbitro.INTERNACIONAL) {
                return false;
            }
        }
        return true;
    }

    public List<Arbitro> getArbitros() {
        return Collections.unmodifiableList(Arrays.asList(principal, asistente1, asistente2));
    }

    public Arbitro getPrincipal() {
        return principal;
    }

    public Arbitro getAsistente1() {
        return asistente1;
    }

    public Arbitro getAsistente2() {
        return asistente2;
    }
}
