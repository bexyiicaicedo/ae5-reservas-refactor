package com.uees.reservas.domain;

public class Usuario {

    private final String nombre;
    private final String correo;
    private final boolean activo;

    public Usuario(String nombre, String correo, boolean activo) {
        this.nombre = nombre;
        this.correo = correo;
        this.activo = activo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public boolean isActivo() {
        return activo;
    }
}
