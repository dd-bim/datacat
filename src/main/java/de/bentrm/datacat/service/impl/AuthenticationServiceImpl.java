package de.bentrm.datacat.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import de.bentrm.datacat.auth.AuthProperties;
import de.bentrm.datacat.auth.JwtPreAuthenticatedAuthenticationToken;
import de.bentrm.datacat.auth.JwtUserDetails;
import de.bentrm.datacat.domain.EmailConfirmationRequest;
import de.bentrm.datacat.domain.Roles;
import de.bentrm.datacat.domain.User;
import de.bentrm.datacat.graphql.dto.SignupInput;
import de.bentrm.datacat.repository.EmailConfirmationRepository;
import de.bentrm.datacat.repository.UserRepository;
import de.bentrm.datacat.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Validated
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final String GENERIC_AUTHENTICATION_ERROR_MSG = "Bad username or password.";

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
    private EmailConfirmationRepository emailConfirmationRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        final AuthProperties.Admin properties = authProperties.getAdmin();
        if (!userRepository.existsByUsername(properties.getUsername())) {
            log.info("No SUPERADMIN user named {} found. Adding user...", properties.getUsername());

            var admin = new User();
            admin.setUsername(properties.getUsername());
            admin.setFirstName(properties.getFirstname());
            admin.setLastName(properties.getLastname());
            admin.setEmail(properties.getEmail());
            admin.setOrganization(properties.getOrganization());
            admin.setPassword(passwordEncoder.encode(properties.getPassword()));
            admin.getRoles().addAll(List.of(Roles.values()));
            admin.setEmailConfirmed(true);
            admin = userRepository.save(admin);

            log.info("Added admin user: {}", admin);
        }
    }

    @Override
    public void signup(SignupInput signupInput) {
        if (userRepository.existsByUsername(signupInput.getUsername())) {
            throw new UsernameTakenException();
        }

        if (userRepository.existsByEmail(signupInput.getEmail())) {
            throw new EmailTakenException();
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

        EmailConfirmationRequest emailConfirmationRequest = emailConfirmationRepository.save(EmailConfirmationRequest.of(user));
        emailService.sendEmailConfirmation(emailConfirmationRequest);
    }

    @Override
    public void fulfillEmailConfirmationRequest(@NotNull String confirmationToken) {
        final EmailConfirmationRequest request = emailConfirmationRepository.findByToken(confirmationToken, 1).orElseThrow();

        if (request.isExpired()) {
            throw new EmailConfirmationException("Confirmation Token is expired.");
        }

        request.fulfill();
        emailConfirmationRepository.save(request);
    }

    @Override
    public String login(@NotBlank String username, @NotBlank String password) {
        final Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            log.info("Bad login request for non-existing username {}.", username);
            publishAuthenticationFailure(username);
            throw new BadCredentialsException(GENERIC_AUTHENTICATION_ERROR_MSG);
        }

        User user = optionalUser.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.info("Bad login request providing wrong password for username {}.", username);
            publishAuthenticationFailure(username);
            throw new BadCredentialsException(GENERIC_AUTHENTICATION_ERROR_MSG);
        }

        if (!user.isEmailConfirmed()) {
            log.info("Bad login request of unconfirmed account {}.", username);
            throw new EmailUnconfirmedException();
        }

        if (!user.isEnabled()) {
            log.info("Bad login request of disabled account {}.", username);
            publishAuthenticationFailure(username);
            throw new DisabledException("User account locked.");
        }

        var authenticationToken = new UsernamePasswordAuthenticationToken(username, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        applicationEventPublisher.publishEvent(new AuditApplicationEvent(Instant.now(), username, AUTHENTICATION_SUCCESS, new HashMap<>()));
        return buildToken(user);
    }

    @Override
    public void login(@NotBlank String token) {
        final DecodedJWT jwt = jwtVerifier.verify(token);
        final String username = jwt.getSubject();

        final User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> {
                    log.info("Bad token: {jwt}");
                    return new UsernameNotFoundException("The provided username is unknown.");
                });

        if (user.isLocked()) {
            log.info("Bad token: {}", jwt);
            throw new LockedException("The account is locked - invalid token.");
        }

        final UserDetails userDetails = new JwtUserDetails(jwt);
        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        final WebAuthenticationDetails webAuthenticationDetails = new WebAuthenticationDetailsSource().buildDetails(request);
        final var authenticationToken = new JwtPreAuthenticatedAuthenticationToken(
                userDetails.getUsername(),
                userDetails.getAuthorities(),
                webAuthenticationDetails);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        applicationEventPublisher.publishEvent(new AuditApplicationEvent(Instant.now(), authenticationToken.getName(), AUTHENTICATION_SUCCESS, new HashMap<>()));
    }

    private void publishAuthenticationFailure(String username) {
        applicationEventPublisher.publishEvent(new AuditApplicationEvent(Instant.now(), username, AUTHENTICATION_FAILURE, new HashMap<>()));
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
