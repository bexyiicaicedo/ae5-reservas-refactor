package com.uees.reservas.notificacion;

import com.uees.reservas.domain.Correo;
import com.uees.reservas.domain.Reserva;
import com.uees.reservas.domain.Usuario;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsabilidad única: notificar al usuario que su reserva fue confirmada.
 *
 * REFACTORIZACIÓN 2 (Value Object) aplicada aquí: en vez de recibir un
 * Predicate<String> externo para validar el correo, ahora construye un
 * Correo (record) en el mismo punto donde antes se ejecutaba la validación
 * ad-hoc. El invariante "qué es un correo válido" vive en un solo lugar
 * (Correo), no repetido en ReservaService y en un predicado inyectado.
 *
 * Importante: el Correo se construye AQUÍ, no en Usuario. Esto preserva el
 * comportamiento exacto que protegían las pruebas: la excepción sigue
 * lanzándose en el momento de notificar, no al crear el Usuario. Un primer
 * intento (validar en el constructor de Usuario) rompió la prueba
 * correoInvalidoLanzaExcepcion porque adelantaba el momento del fallo; fue
 * revertido antes de llegar a commit (ver reporte técnico, "Riesgo
 * detectado por la red de pruebas").
 */
public class NotificadorReserva {

    private final List<String> correosEnviados = new ArrayList<>();

    public String notificar(Usuario usuario, Reserva reserva) {
        Correo correo = new Correo(usuario.getCorreo());
        String mensaje = "Reserva " + reserva.getId() + " confirmada para " + usuario.getNombre();
        correosEnviados.add(correo + " -> " + mensaje);
        return mensaje;
    }

    public List<String> obtenerEnviados() {
        return correosEnviados;
    }
}
