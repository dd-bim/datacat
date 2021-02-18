package de.bentrm.datacat.auth.domain;

import org.springframework.security.core.GrantedAuthority;

/**
 * Available user roles.
 */
public enum Role implements GrantedAuthority {

    SUPERADMIN,
    ADMIN,
    USER,
    READONLY;

    @Override
    public final String getAuthority() {
        return "ROLE_" + this.name();
    }
}
