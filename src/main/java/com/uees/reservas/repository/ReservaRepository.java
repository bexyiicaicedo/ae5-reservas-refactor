package com.uees.reservas.repository;

import com.uees.reservas.domain.Reserva;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsabilidad única: persistir y recuperar reservas.
 * Extraída de ReservaService (Extract Class) porque "dónde se guardan los
 * datos" es una razón de cambio distinta de "cuándo una reserva puede
 * confirmarse".
 */
public class ReservaRepository {

    private final List<Reserva> reservas = new ArrayList<>();

    public void guardar(Reserva reserva) {
        reservas.add(reserva);
    }

    public List<Reserva> obtenerTodas() {
        return reservas;
    }
}
