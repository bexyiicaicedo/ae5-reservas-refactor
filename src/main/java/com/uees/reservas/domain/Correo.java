package com.uees.reservas.domain;

public record Correo(String valor) {
    public Correo {
        if (!esValido(valor)) {
            throw new IllegalArgumentException("Correo inválido: " + valor);
        }
    }

    public static boolean esValido(String valor) {
        if (valor == null) {
            return false;
        }
        int posArroba = valor.indexOf('@');
        if (posArroba <= 0) {
            return false;
        }
        String dominio = valor.substring(posArroba + 1);
        return dominio.contains(".");
    }

    @Override
    public String toString() {
        return valor;
    }
}
