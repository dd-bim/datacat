package de.bentrm.datacat.auth;

import de.bentrm.datacat.auth.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.function.Predicate.not;

/**
 * Spring web filter that checks every request for the existence of
 * a JWT-token in the header of the request.
 *
 * If a token is found, it will be validated.
 * This filter handles legacy/custom JWT tokens. Keycloak tokens are handled by OAuth2 Resource Server.
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final Pattern BEARER_PATTERN = Pattern.compile("^Bearer (.+?)$");

    @Autowired @Lazy
    private AuthenticationService authenticationService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        Optional<String> token = getToken(request);
        
        // Versuche nur den eigenen JWT zu validieren, wenn ein Token vorhanden ist
        // Der OAuth2 Resource Server wird Keycloak-Tokens automatisch verarbeiten
        if (token.isPresent()) {
            try {
                authenticationService.login(token.get());
                // Wenn Login erfolgreich, wurde Authentication gesetzt
                // Das signalisiert dem OAuth2 Resource Server, dass er nicht mehr prüfen muss
            } catch (Exception e) {
                // Falls eigener JWT-Verifier fehlschlägt, könnte es ein Keycloak-Token sein
                // In diesem Fall lassen wir den OAuth2 Resource Server weitermachen
                // (keine Exception werfen, damit OAuth2 Resource Server den Token prüfen kann)
            }
        }
        
        filterChain.doFilter(request, response);
    }

    private Optional<String> getToken(HttpServletRequest request) {
        return Optional
                .ofNullable(request.getHeader(AUTHORIZATION_HEADER))
                .filter(not(String::isEmpty))
                .map(BEARER_PATTERN::matcher)
                .filter(Matcher::find)
                .map(matcher -> matcher.group(1));
    }
}
