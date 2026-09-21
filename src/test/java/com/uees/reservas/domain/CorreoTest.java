package com.uees.reservas.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas del Value Object Correo, introducido en la Refactorización 2.
 * Fijan el contrato de "qué es un correo válido" en un solo lugar, para
 * que ninguna otra clase vuelva a reimplementar esta regla.
 */
class CorreoTest {

    @Test
    void construyeCorreoValidoSinLanzarExcepcion() {
        Correo correo = new Correo("ruth.caicedo@uees.edu.ec");
        assertEquals("ruth.caicedo@uees.edu.ec", correo.valor());
    }

    @Test
    void rechazaCorreoNulo() {
        assertThrows(IllegalArgumentException.class, () -> new Correo(null));
    }

    @Test
    void rechazaCorreoSinArroba() {
        assertThrows(IllegalArgumentException.class, () -> new Correo("ruth.correo.com"));
    }

    @Test
    void rechazaCorreoQueEmpiezaConArroba() {
        assertThrows(IllegalArgumentException.class, () -> new Correo("@dominio.com"));
    }

    @Test
    void rechazaCorreoSinPuntoEnElDominio() {
        assertThrows(IllegalArgumentException.class, () -> new Correo("ruth@dominio"));
    }

    @Test
    void esValidoNoLanzaExcepcionYPermiteConsultarSinConstruir() {
        assertTrue(Correo.esValido("ruth.caicedo@uees.edu.ec"));
        assertFalse(Correo.esValido("correo-sin-formato"));
    }

    @Test
    void toStringDevuelveElValorPlano() {
        Correo correo = new Correo("ruth.caicedo@uees.edu.ec");
        assertEquals("ruth.caicedo@uees.edu.ec", correo.toString());
    }
}
