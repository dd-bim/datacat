package de.bentrm.datacat.auth;

import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.ArrayList;

public class JwtPreAuthenticatedAuthenticationToken extends PreAuthenticatedAuthenticationToken {
    public JwtPreAuthenticatedAuthenticationToken(String principal, WebAuthenticationDetails details) {
        super(principal, null, new ArrayList<>());
        super.setDetails(details);
    }
}
