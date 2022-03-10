package ru.mhelper.models;

public enum ERole {

    ROLE_USER("USER"),
    ROLE_ADMIN("ADMIN"),
    ROLE_TELEGRAM("TELEGRAM"),
    CHROME_EXTENSION("CHROME_EXTENSION");

    private final String name;

    public String getName() {
        return name;
    }

    ERole(String name) {
        this.name = name;
    }
}
