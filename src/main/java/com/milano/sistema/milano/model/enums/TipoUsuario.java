package com.milano.sistema.milano.model.enums;

public enum TipoUsuario {
    CLIENTE("ROLE_CLIENTE"),
    GERENTE("ROLE_GERENTE"),
    ADMIN("ROLE_ADMIN");

    private final String role;

    TipoUsuario(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}