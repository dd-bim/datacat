package de.bentrm.datacat.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Converter für Keycloak JWT-Tokens, der die Rollen aus dem Token extrahiert
 * und in Spring Security GrantedAuthorities umwandelt.
 */
@Slf4j
@Component
public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        Collection<GrantedAuthority> authorities = extractAuthorities(jwt);
        return new JwtAuthenticationToken(jwt, authorities);
    }

    /**
     * Extrahiert Rollen aus verschiedenen möglichen Keycloak-Claims:
     * 1. realm_access.roles (Realm-weite Rollen)
     * 2. resource_access.<client-id>.roles (Client-spezifische Rollen)
     * 3. roles (falls direkt im Token)
     */
    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        Set<String> roles = new HashSet<>();

        // Realm Rollen extrahieren
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess != null && realmAccess.get("roles") instanceof Collection<?>) {
            Collection<?> realmRoles = (Collection<?>) realmAccess.get("roles");
            realmRoles.stream()
                    .filter(role -> role instanceof String)
                    .map(String.class::cast)
                    .forEach(roles::add);
        }

        // Resource/Client Rollen extrahieren
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess != null) {
            resourceAccess.values().stream()
                    .filter(resource -> resource instanceof Map)
                    .flatMap(resource -> {
                        Object rolesObj = ((Map<?, ?>) resource).get("roles");
                        if (rolesObj instanceof Collection<?>) {
                            return ((Collection<?>) rolesObj).stream()
                                    .filter(role -> role instanceof String)
                                    .map(String.class::cast);
                        }
                        return Stream.empty();
                    })
                    .forEach(roles::add);
        }

        // Fallback: Direkte roles im Token
        Collection<String> directRoles = jwt.getClaim("roles");
        if (directRoles != null) {
            roles.addAll(directRoles);
        }

        // Konvertiere zu GrantedAuthorities
        // Spring Security's hasRole() sucht nach "ROLE_" + Rollenname
        // Also hasRole('ADMIN') → sucht nach Authority "ROLE_ADMIN"
        return roles.stream()
                .map(role -> {
                    String normalized = role.toUpperCase();
                    return normalized.startsWith("ROLE_") ? normalized : "ROLE_" + normalized;
                })
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }
}
