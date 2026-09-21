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

    private final ReservaRepository repositorio;
    private final NotificadorReserva notificador;

    public ReservaService() {
        this.repositorio = new ReservaRepository();
        this.notificador = new NotificadorReserva();
    }

    public boolean validar(String correo) {
        return Correo.esValido(correo);
    }

    public void confirmar(Usuario usuario, Reserva reserva, int horasAnticipacion) {
        if (usuario != null) {
            if (usuario.isActivo()) {
                if (!reserva.isCancelada()) {
                    if (horasAnticipacion >= 2) {
                        reserva.confirmar();
                        guardar(reserva);
                        enviarCorreo(usuario, reserva);
                    }
                }
            }
        }
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
