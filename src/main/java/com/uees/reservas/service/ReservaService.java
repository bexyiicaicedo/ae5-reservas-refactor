package com.uees.reservas.service;

import com.uees.reservas.domain.Correo;
import com.uees.reservas.domain.Reserva;
import com.uees.reservas.domain.Usuario;
import com.uees.reservas.notificacion.NotificadorReserva;
import com.uees.reservas.repository.ReservaRepository;

import java.util.List;

/**
 * Orquesta la confirmación de una reserva.
 *
 * REFACTORIZACIÓN 1 (Extract Class / Move Method): la persistencia se
 * movió a ReservaRepository y la notificación a NotificadorReserva.
 * ReservaService ya no sabe CÓMO se guarda ni CÓMO se notifica; solo
 * decide CUÁNDO debe ocurrir cada cosa.
 *
 * REFACTORIZACIÓN 2 (Value Object): validar() ya no reimplementa las
 * reglas de "qué es un correo válido"; delega en Correo.esValido(), que es
 * ahora la única fuente de verdad sobre esa regla (antes vivía duplicada
 * aquí y en el predicado que se inyectaba a NotificadorReserva).
 *
 * La API pública (validar, confirmar, guardar, enviarCorreo, getReservas,
 * getCorreosEnviados) se mantiene idéntica para que la suite de pruebas de
 * la Actividad 2 siga protegiendo el comportamiento sin modificaciones.
 */
public class ReservaService {

    private static final int HORAS_MINIMAS_ANTICIPACION = 2;

    private final ReservaRepository repositorio;
    private final NotificadorReserva notificador;

    public ReservaService() {
        this.repositorio = new ReservaRepository();
        this.notificador = new NotificadorReserva();
    }

    public boolean validar(String correo) {
        return Correo.esValido(correo);
    }

    /**
     * REFACTORIZACIÓN 3 (Guard Clauses / Decompose Conditional): el
     * condicional anidado de 4 niveles se reemplazó por salidas
     * anticipadas. Cada guard clause nombra explícitamente la razón por
     * la que NO se confirma, en vez de forzar al lector a acumular cuatro
     * condiciones en la cabeza para entender el único camino feliz.
     * El resultado final —qué combinaciones confirman la reserva— es
     * idéntico al original; solo cambió cómo se expresa.
     */
    public void confirmar(Usuario usuario, Reserva reserva, int horasAnticipacion) {
        if (usuario == null) {
            return;
        }
        if (!usuario.isActivo()) {
            return;
        }
        if (reserva.isCancelada()) {
            return;
        }
        if (horasAnticipacion < HORAS_MINIMAS_ANTICIPACION) {
            return;
        }

        reserva.confirmar();
        guardar(reserva);
        enviarCorreo(usuario, reserva);
    }

    public void guardar(Reserva reserva) {
        repositorio.guardar(reserva);
    }

    public String enviarCorreo(Usuario usuario, Reserva reserva) {
        return notificador.notificar(usuario, reserva);
    }

    public List<Reserva> getReservas() {
        return repositorio.obtenerTodas();
    }

    public List<String> getCorreosEnviados() {
        return notificador.obtenerEnviados();
    }
}
