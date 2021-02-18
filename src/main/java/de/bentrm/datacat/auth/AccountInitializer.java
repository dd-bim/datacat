package de.bentrm.datacat.auth;

import de.bentrm.datacat.auth.domain.User;
import de.bentrm.datacat.base.repository.UserRepository;
import de.bentrm.datacat.properties.AppProperties;
import de.bentrm.datacat.properties.PropertyMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Custom account initialization service that creates @{@link User}s on first application
 * boot. This is a helper mechanism to initialize the first admin user and the like.
 * In most cases, a user should be registered via the on-boarding routine of the API.
 */
@Slf4j
@Service
public class AccountInitializer implements ApplicationRunner {

    private final PropertyMapper propertyMapper;
    private final AppProperties properties;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AccountInitializer(PropertyMapper propertyMapper, AppProperties properties, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.propertyMapper = propertyMapper;
        this.properties = properties;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("Running account initialization...");
        final var users = properties.getUsers();

        for (var entry : users.entrySet()) {
            final String username = entry.getKey();
            final var userProperties = entry.getValue();

            final Optional<User> match = userRepository.findByUsername(username);
            log.debug("Current user with username {}: {}", username, match);
            if (match.isPresent()) {
                log.info("Account with username {} found... skipping account creation.", username);
                continue;
            }

            log.info("Adding new account '{}'...", username);
            final User user = propertyMapper.toUser(username, userProperties);

            String password = userProperties.getPassword();
            if (password == null || password.isBlank()) {
                password = PasswordGenerator.generate();
                log.info("Generated password for user {}: {}", username, password);
            }
            String encodedPassword = passwordEncoder.encode(password);
            user.setPassword(encodedPassword);

            user.setLocked(false);
            user.setEmailConfirmed(true);
            user.setCredentialsExpired(false);
            user.setExpired(false);
            userRepository.save(user);
        }
    }
}
