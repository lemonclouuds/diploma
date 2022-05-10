package com.company.financeApp.domain.user;

public enum UserPermission {
    USER_READ("user:read"),
    USER_WRITE("user:write"),
    TRANSACTION_READ("transaction:read"),
    TRANSACTION_WRITE("transaction:write"),
    CATEGORY_READ("category:write"),
    CATEGORY_WRITE("category:read");

    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
