package com.tppoo.model.torneos_entidades;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** Árbitro principal y dos asistentes, todos distintos. */
public class TernaArbitral implements Serializable {
    private final Arbitro principal;
    private final Arbitro asistente1;
    private final Arbitro asistente2;

    public TernaArbitral(Arbitro principal, Arbitro asistente1, Arbitro asistente2) {
        if (principal == null || asistente1 == null || asistente2 == null) {
            throw new IllegalArgumentException("La terna arbitral requiere un principal y dos asistentes.");
        }
        if (principal.equals(asistente1) || principal.equals(asistente2) || asistente1.equals(asistente2)) {
            throw new IllegalArgumentException("Los tres árbitros de la terna deben ser distintos.");
        }
        this.principal = principal;
        this.asistente1 = asistente1;
        this.asistente2 = asistente2;
    }

    public boolean contiene(Arbitro arbitro) {
        return principal.equals(arbitro) || asistente1.equals(arbitro) || asistente2.equals(arbitro);
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
