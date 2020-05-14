package de.bentrm.datacat.auth;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class JwtUserDetails implements UserDetails {

    private final DecodedJWT decodedJwt;

    public JwtUserDetails(DecodedJWT decodedJwt) {
        this.decodedJwt = decodedJwt;
    }

    @Override
    public List<SimpleGrantedAuthority> getAuthorities() {
        final Claim roles = decodedJwt.getClaim("roles");
        return Arrays
                .stream(roles.asArray(String.class))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return decodedJwt.getSubject();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return Date.from(Instant.now()).compareTo(decodedJwt.getExpiresAt()) > 0;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
