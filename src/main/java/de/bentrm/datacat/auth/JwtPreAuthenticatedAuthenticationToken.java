package de.bentrm.datacat.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Collection;

public class JwtPreAuthenticatedAuthenticationToken extends PreAuthenticatedAuthenticationToken {

    public JwtPreAuthenticatedAuthenticationToken(String aPrincipal, Collection<? extends GrantedAuthority> anAuthorities, WebAuthenticationDetails details) {
        super(aPrincipal, null, anAuthorities);
        super.setDetails(details);
    }
}
