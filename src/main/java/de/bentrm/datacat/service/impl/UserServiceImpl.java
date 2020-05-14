package de.bentrm.datacat.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import de.bentrm.datacat.auth.UserProfile;
import de.bentrm.datacat.auth.UserSession;
import de.bentrm.datacat.domain.Roles;
import de.bentrm.datacat.domain.User;
import de.bentrm.datacat.graphql.dto.SignupInput;
import de.bentrm.datacat.repository.UserRepository;
import de.bentrm.datacat.service.UserService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Service
@Validated
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private Logger logger;

    @Value("${de.bentrm.datacat.auth.issuer}")
    private String issuer;

    @Value("${de.bentrm.datacat.auth.admin.username:admin}")
    private String adminUsername;

    @Value("${de.bentrm.datacat.auth.admin.password}")
    private String adminPassword;

    @Autowired
    private Algorithm algorithm;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!userRepository.existsByUsername(adminUsername)) {
            logger.info("No superadmin user named {} found. Adding user...", adminUsername);

            var admin = new User();
            admin.setUsername(adminUsername);
            admin.setFirstName("");
            admin.setLastName("");
            admin.setEmail("");
            admin.setOrganization("");
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.getRoles().add(Roles.ROLE_SUPERADMIN);
            admin.getRoles().add(Roles.ROLE_ADMIN);
            admin.getRoles().add(Roles.ROLE_USER);
            admin = userRepository.save(admin);

            logger.info("Added admin user: {}", admin);
        }
    }

    @Override
    public UserSession signup(SignupInput signupInput) {
        logger.debug("New signup: {}", signupInput);

        if (userRepository.existsByUsername(signupInput.getUsername())) {
            throw new IllegalArgumentException("Username is taken.");
        }

        if (userRepository.existsByEmail(signupInput.getEmail())) {
            throw new IllegalArgumentException("Email is taken.");
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
        user.getRoles().add(Roles.ROLE_USER);

        // login new user
        var authenticationToken = new UsernamePasswordAuthenticationToken(signupInput.getUsername(), null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // save after logging user in to audit creation
        user = userRepository.save(user);

        String token = getToken(user);
        UserProfile newUserProfile = UserProfile.of(user);
        return new UserSession(token, newUserProfile);
    }

    @Override
    public UserSession login(@NotBlank String username, @NotBlank String password) {
        logger.debug("{} is trying to login", username);
        try {
            final User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Unknown username."));
            logger.debug("{}", user);
            logger.debug("Password: {}", password);
            logger.debug("Encoded password: {}", user.getPassword());
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new BadCredentialsException("Wrong password.");
            }
            var authenticationToken = new UsernamePasswordAuthenticationToken(username, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            String token = getToken(user);
            UserProfile newUserProfile = UserProfile.of(user);
            return new UserSession(token, newUserProfile);
        } catch (AuthenticationException ex) {
            throw new BadCredentialsException("Username not found.");
        }
    }

    public String getToken(UserDetails user) {
        Instant now = Instant.now();
        Instant expiry = Instant.now().plus(Duration.ofHours(2)); // Token will be valid for 2 hours
        final String[] claims = user.getAuthorities().stream().map(Object::toString).toArray(String[]::new);
        return JWT
                .create()
                .withIssuer(issuer) // Same as within the JWTVerifier
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(expiry))
                .withSubject(user.getUsername())
                .withArrayClaim("roles", claims)
                .sign(algorithm); // Same algorithm as within the JWTVerifier
    }
}
