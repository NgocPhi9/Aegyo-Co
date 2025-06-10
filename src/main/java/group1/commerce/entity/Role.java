package group1.commerce.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    CUSTOMER,
    ADMIN;

    @Override
    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}

