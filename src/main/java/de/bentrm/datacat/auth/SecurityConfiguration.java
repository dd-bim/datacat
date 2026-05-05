package de.bentrm.datacat.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {  
    private final JwtFilter jwtFilter;
    private final KeycloakJwtAuthenticationConverter keycloakJwtAuthenticationConverter;

    public SecurityConfiguration(
            JwtFilter jwtFilter, 
            KeycloakJwtAuthenticationConverter keycloakJwtAuthenticationConverter) {
        this.jwtFilter = jwtFilter;
        this.keycloakJwtAuthenticationConverter = keycloakJwtAuthenticationConverter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> requests
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/graphql").permitAll())
                // Eigener JWT-Filter für legacy Tokens (läuft vor OAuth2)
                .addFilterBefore(jwtFilter, RequestHeaderAuthenticationFilter.class)
                // OAuth2 Resource Server für Keycloak JWT-Tokens (nur wenn JwtFilter keine Auth gesetzt hat)
                .oauth2ResourceServer(oauth2 -> oauth2
                        .bearerTokenResolver(conditionalBearerTokenResolver())
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(keycloakJwtAuthenticationConverter)
                        )
                );

        return http.build();
    }
    
    /**
     * BearerTokenResolver der nur dann einen Token zurückgibt,
     * wenn noch keine Authentication im SecurityContext vorhanden ist.
     * Dies verhindert, dass der OAuth2 Resource Server Legacy-JWT-Tokens behandelt.
     */
    @Bean
    public BearerTokenResolver conditionalBearerTokenResolver() {
        DefaultBearerTokenResolver defaultResolver = new DefaultBearerTokenResolver();
        return request -> {
            // Wenn bereits eine Authentication existiert (durch JwtFilter gesetzt),
            // gib keinen Token zurück -> OAuth2 Resource Server wird übersprungen
            if (SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
                return null;
            }
            // Ansonsten normal Token auflösen für Keycloak
            return defaultResolver.resolve(request);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    
    /**
     * Definiert die Rollen-Hierarchie: ADMIN > USER > READONLY
     * Ein Admin hat automatisch alle Rechte von USER und READONLY
     */
    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.fromHierarchy(
            "ROLE_ADMIN > ROLE_USER\n" +
            "ROLE_USER > ROLE_READONLY"
        );
    }
    
    /**
     * Registriert den Custom MethodSecurityExpressionHandler für korrekte principal-Auflösung
     */
    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
        CustomAuthenticationPrincipalResolver handler = new CustomAuthenticationPrincipalResolver();
        handler.setRoleHierarchy(roleHierarchy);
        return handler;
    }
}
