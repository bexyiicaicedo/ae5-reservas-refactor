package com.uees.reservas.domain;

public class Reserva {

    private final String id;
    private boolean cancelada;
    private boolean confirmada;

    public Reserva(String id) {
        this.id = id;
        this.cancelada = false;
        this.confirmada = false;
    }

    public String getId() {
        return id;
    }

    public boolean isCancelada() {
        return cancelada;
    }

    public void cancelar() {
        this.cancelada = true;
    }

    public boolean isConfirmada() {
        return confirmada;
    }

    public void confirmar() {
        this.confirmada = true;
    }
}
