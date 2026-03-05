package com.autohub_api.model.enums;

public enum UserRole {
    LEITOR("LEITOR"),     
    ESCRITOR("ESCRITOR");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

