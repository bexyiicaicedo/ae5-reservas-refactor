package com.uees.reservas.service;

import com.uees.reservas.domain.Reserva;
import com.uees.reservas.domain.Usuario;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase heredada (legacy).
 *
 * Problemas de diseño detectados (ver reporte técnico):
 *  1. Mezcla responsabilidades: validación de correo, persistencia en memoria
 *     y notificación (impresión de correo) conviven en la misma clase.
 *  2. confirmar() usa un condicional anidado de 4 niveles (árbol de flechas).
 *  3. El correo se maneja como String primitivo (primitive obsession) y su
 *     validación se invoca desde dos sitios distintos sin una única fuente
 *     de verdad sobre qué es un correo válido.
 */
public class ReservaService {

    private final List<Reserva> reservas = new ArrayList<>();
    private final List<String> correosEnviados = new ArrayList<>();

    public boolean validar(String correo) {
        if (correo == null) {
            return false;
        }
        int posArroba = correo.indexOf('@');
        if (posArroba <= 0) {
            return false;
        }
        String dominio = correo.substring(posArroba + 1);
        if (!dominio.contains(".")) {
            return false;
        }
        return true;
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
        reservas.add(reserva);
    }

    public String enviarCorreo(Usuario usuario, Reserva reserva) {
        if (!validar(usuario.getCorreo())) {
            throw new IllegalArgumentException("Correo inválido: " + usuario.getCorreo());
        }
        String mensaje = "Reserva " + reserva.getId() + " confirmada para " + usuario.getNombre();
        correosEnviados.add(usuario.getCorreo() + " -> " + mensaje);
        return mensaje;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public List<String> getCorreosEnviados() {
        return correosEnviados;
    }
}
