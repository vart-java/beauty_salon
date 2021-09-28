package com.artuhin.sproject.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN,
    MASTER,
    CLIENT;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}
