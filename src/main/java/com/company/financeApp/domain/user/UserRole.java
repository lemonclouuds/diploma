package com.company.financeApp.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public enum UserRole {
    ADMIN(Set.of(UserPermission.TRANSACTION_READ, UserPermission.TRANSACTION_WRITE,
            UserPermission.USER_READ, UserPermission.USER_WRITE,
            UserPermission.CATEGORY_READ, UserPermission.CATEGORY_WRITE)),

    USER(Set.of(UserPermission.TRANSACTION_READ, UserPermission.USER_READ,
            UserPermission.CATEGORY_READ, UserPermission.CATEGORY_WRITE));

    @Getter
    private final Set<UserPermission> permissions;

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream().map(
                permission -> new SimpleGrantedAuthority(permission.getPermission())
        ).collect(Collectors.toSet());
    }
}
