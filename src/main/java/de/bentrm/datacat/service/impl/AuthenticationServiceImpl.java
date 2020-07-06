package de.bentrm.datacat.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import de.bentrm.datacat.auth.AuthProperties;
import de.bentrm.datacat.auth.JwtPreAuthenticatedAuthenticationToken;
import de.bentrm.datacat.auth.JwtUserDetails;
import de.bentrm.datacat.domain.Roles;
import de.bentrm.datacat.domain.User;
import de.bentrm.datacat.graphql.dto.SignupInput;
import de.bentrm.datacat.repository.UserRepository;
import de.bentrm.datacat.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Validated
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    public static final String AUTHENTICATION_SUCCESS = "AUTHENTICATION_SUCCESS";
    public static final String AUTHENTICATION_FAILURE = "AUTHENTICATION_FAILURE";

    @Autowired
    private AuthProperties authProperties;

    @Autowired
    private JWTVerifier jwtVerifier;

    @Autowired
    private Algorithm algorithm;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        final AuthProperties.Admin properties = authProperties.getAdmin();
        if (!userRepository.existsByUsername(properties.getUsername())) {
            log.info("No superadmin user named {} found. Adding user...", properties.getUsername());

            var admin = new User();
            admin.setUsername(properties.getUsername());
            admin.setFirstName(properties.getFirstname());
            admin.setLastName(properties.getLastname());
            admin.setEmail(properties.getEmail());
            admin.setOrganization(properties.getOrganization());
            admin.setPassword(passwordEncoder.encode(properties.getPassword()));
            admin.getRoles().addAll(List.of(Roles.values()));

            admin = userRepository.save(admin);

            log.info("Added admin user: {}", admin);
        }
    }

    @Override
    public String signup(SignupInput signupInput) {
        log.debug("New signup: {}", signupInput);

        if (userRepository.existsByUsername(signupInput.getUsername())) {
            final IllegalArgumentException exception = new IllegalArgumentException("Username is taken.");
            final AuditApplicationEvent event = new AuditApplicationEvent(Instant.now(), signupInput.getUsername(), AUTHENTICATION_SUCCESS, Map.ofEntries(Map.entry("details", exception)));
            applicationEventPublisher.publishEvent(event);
            throw exception;
        }

        if (userRepository.existsByEmail(signupInput.getEmail())) {
            final IllegalArgumentException exception = new IllegalArgumentException("Email is taken.");
            final AuditApplicationEvent event = new AuditApplicationEvent(Instant.now(), signupInput.getUsername(), AUTHENTICATION_SUCCESS, Map.ofEntries(Map.entry("details", exception)));
            applicationEventPublisher.publishEvent(event);
            throw exception;
        }

        // create new user
        var user = new User();
        user.setUsername(signupInput.getUsername());
        user.setFirstName(signupInput.getFirstName());
        user.setLastName(signupInput.getLastName());
        user.setEmail(signupInput.getEmail());
        user.setOrganization(signupInput.getOrganization());

        String encodedPassword = passwordEncoder.encode(signupInput.getPassword());
        user.setPassword(encodedPassword);
        user.getRoles().add(Roles.ROLE_READONLY);

        // login new user
        var authenticationToken = new UsernamePasswordAuthenticationToken(signupInput.getUsername(), null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // save after logging user in to audit creation
        user = userRepository.save(user);

        String token = buildToken(user);

        applicationEventPublisher.publishEvent(new AuditApplicationEvent(Instant.now(), user.getUsername(), AUTHENTICATION_SUCCESS, new HashMap<>()));
        return token;
    }

    @Override
    public String login(@NotBlank String username, @NotBlank String password) {
        try {
            final User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Unknown username."));
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new BadCredentialsException("Wrong password.");
            }
            var authenticationToken = new UsernamePasswordAuthenticationToken(username, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            String token = buildToken(user);
            applicationEventPublisher.publishEvent(new AuditApplicationEvent(Instant.now(), username, AUTHENTICATION_SUCCESS, new HashMap<>()));
            return token;
        } catch (AuthenticationException ex) {
            applicationEventPublisher.publishEvent(new AuditApplicationEvent(Instant.now(), username, AUTHENTICATION_FAILURE, new HashMap<>()));
            throw new BadCredentialsException("Unknown username or wrong password provided.", ex);
        }
    }

    @Override
    public void login(@NotBlank String token) {
        final DecodedJWT jwt = jwtVerifier.verify(token);
        final UserDetails userDetails = new JwtUserDetails(jwt);
        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        final WebAuthenticationDetails webAuthenticationDetails = new WebAuthenticationDetailsSource().buildDetails(request);
        final var authenticationToken = new JwtPreAuthenticatedAuthenticationToken(
                userDetails.getUsername(),
                userDetails.getAuthorities(),
                webAuthenticationDetails);
        log.debug("Auth token: {}", authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        applicationEventPublisher.publishEvent(new AuditApplicationEvent(Instant.now(), authenticationToken.getName(), AUTHENTICATION_SUCCESS, new HashMap<>()));
    }

    public String buildToken(UserDetails user) {
        Instant now = Instant.now();
        Instant expiry = Instant.now().plus(Duration.ofHours(4)); // Token will be valid for 4 hours
        final String[] claims = user.getAuthorities().stream().map(Object::toString).toArray(String[]::new);
        return JWT
                .create()
                .withIssuer(authProperties.getIssuer()) // Same as within the JWTVerifier
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(expiry))
                .withSubject(user.getUsername())
                .withArrayClaim("roles", claims)
                .sign(algorithm); // Same algorithm as within the JWTVerifier
    }
}
