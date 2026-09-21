package com.uees.reservas.notificacion;

import com.uees.reservas.domain.Reserva;
import com.uees.reservas.domain.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Responsabilidad única: notificar al usuario que su reserva fue confirmada.
 * Extraída de ReservaService (Extract Class + Move Method) porque "cómo se
 * avisa al usuario" es una razón de cambio distinta de "cuándo una reserva
 * puede confirmarse".
 *
 * Recibe el validador de correo por composición (en vez de conocer la
 * implementación de ReservaService) para no acoplarse a ella; en la
 * Refactorización 2 este validador será sustituido por el Value Object
 * Correo.
 */
public class NotificadorReserva {

    private final List<String> correosEnviados = new ArrayList<>();
    private final Predicate<String> correoEsValido;

    public NotificadorReserva(Predicate<String> correoEsValido) {
        this.correoEsValido = correoEsValido;
    }

    public String notificar(Usuario usuario, Reserva reserva) {
        if (!correoEsValido.test(usuario.getCorreo())) {
            throw new IllegalArgumentException("Correo inválido: " + usuario.getCorreo());
        }
        String mensaje = "Reserva " + reserva.getId() + " confirmada para " + usuario.getNombre();
        correosEnviados.add(usuario.getCorreo() + " -> " + mensaje);
        return mensaje;
    }

    public List<String> obtenerEnviados() {
        return correosEnviados;
    }
}
