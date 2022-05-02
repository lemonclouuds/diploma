package com.company.financeApp.domain;

public enum UserPermission {
    USER_READ("user:read"),
    USER_WRITE("user:write"),
    TRANSACTION_READ("transaction:read"),
    TRANSACTION_WRITE("transaction:write"),
    RATING_READ("rating:write"),
    RATING_WRITE("rating:read");

    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
