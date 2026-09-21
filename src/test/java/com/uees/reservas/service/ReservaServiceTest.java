package com.uees.reservas.service;

import com.uees.reservas.domain.Reserva;
import com.uees.reservas.domain.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Red de seguridad (Actividad 2) — pruebas de caracterización.
 * Deben permanecer en VERDE antes, durante y después de cada refactorización
 * de la Actividad 5. Ninguna prueba de este archivo cambia su intención al
 * refactorizar; a lo sumo cambia la forma en que se invoca la aserción
 * cuando el comportamiento se traslada a una nueva clase/tipo.
 */
class ReservaServiceTest {

    private ReservaService service;

    @BeforeEach
    void setUp() {
        service = new ReservaService();
    }

    @Nested
    @DisplayName("Validación de correo")
    class ValidarCorreo {

        @Test
        @DisplayName("correo nulo no es válido")
        void correoNuloNoEsValido() {
            assertFalse(service.validar(null));
        }

        @Test
        @DisplayName("correo sin @ no es válido")
        void correoSinArrobaNoEsValido() {
            assertFalse(service.validar("ruth.correo.com"));
        }

        @Test
        @DisplayName("correo que empieza con @ no es válido")
        void correoQueEmpiezaConArrobaNoEsValido() {
            assertFalse(service.validar("@dominio.com"));
        }

        @Test
        @DisplayName("correo sin punto en el dominio no es válido")
        void correoSinPuntoEnDominioNoEsValido() {
            assertFalse(service.validar("ruth@dominio"));
        }

        @Test
        @DisplayName("correo con formato correcto es válido")
        void correoValido() {
            assertTrue(service.validar("ruth.caicedo@uees.edu.ec"));
        }
    }

    @Nested
    @DisplayName("Confirmación de reserva")
    class ConfirmarReserva {

        @Test
        @DisplayName("si el usuario es nulo, la reserva no se confirma")
        void usuarioNuloNoConfirma() {
            Reserva reserva = new Reserva("R1");
            service.confirmar(null, reserva, 5);
            assertFalse(reserva.isConfirmada());
            assertTrue(service.getReservas().isEmpty());
        }

        @Test
        @DisplayName("si el usuario está inactivo, la reserva no se confirma")
        void usuarioInactivoNoConfirma() {
            Usuario usuario = new Usuario("Ruth", "ruth.caicedo@uees.edu.ec", false);
            Reserva reserva = new Reserva("R1");
            service.confirmar(usuario, reserva, 5);
            assertFalse(reserva.isConfirmada());
        }

        @Test
        @DisplayName("si la reserva está cancelada, no se confirma")
        void reservaCanceladaNoConfirma() {
            Usuario usuario = new Usuario("Ruth", "ruth.caicedo@uees.edu.ec", true);
            Reserva reserva = new Reserva("R1");
            reserva.cancelar();
            service.confirmar(usuario, reserva, 5);
            assertFalse(reserva.isConfirmada());
        }

        @Test
        @DisplayName("si la anticipación es menor a 2 horas, no se confirma")
        void anticipacionInsuficienteNoConfirma() {
            Usuario usuario = new Usuario("Ruth", "ruth.caicedo@uees.edu.ec", true);
            Reserva reserva = new Reserva("R1");
            service.confirmar(usuario, reserva, 1);
            assertFalse(reserva.isConfirmada());
        }

        @Test
        @DisplayName("con usuario activo, reserva no cancelada y anticipación suficiente, se confirma, se guarda y se notifica")
        void casoFelizConfirmaGuardaYNotifica() {
            Usuario usuario = new Usuario("Ruth", "ruth.caicedo@uees.edu.ec", true);
            Reserva reserva = new Reserva("R1");
            service.confirmar(usuario, reserva, 2);

            assertTrue(reserva.isConfirmada());
            assertEquals(1, service.getReservas().size());
            assertEquals(1, service.getCorreosEnviados().size());
            assertTrue(service.getCorreosEnviados().get(0).contains("R1"));
        }
    }

    @Nested
    @DisplayName("Envío de correo")
    class EnviarCorreo {

        @Test
        @DisplayName("lanza excepción si el correo del usuario es inválido")
        void correoInvalidoLanzaExcepcion() {
            Usuario usuario = new Usuario("Ruth", "correo-sin-formato", true);
            Reserva reserva = new Reserva("R2");
            assertThrows(IllegalArgumentException.class,
                    () -> service.enviarCorreo(usuario, reserva));
        }

        @Test
        @DisplayName("devuelve el mensaje de confirmación cuando el correo es válido")
        void correoValidoDevuelveMensaje() {
            Usuario usuario = new Usuario("Ruth", "ruth.caicedo@uees.edu.ec", true);
            Reserva reserva = new Reserva("R3");
            String mensaje = service.enviarCorreo(usuario, reserva);
            assertTrue(mensaje.contains("R3"));
            assertTrue(mensaje.contains("Ruth"));
        }
    }
}
