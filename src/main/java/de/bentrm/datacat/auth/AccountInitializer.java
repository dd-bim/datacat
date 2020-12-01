package de.bentrm.datacat.auth;

import de.bentrm.datacat.auth.domain.User;
import de.bentrm.datacat.base.repository.UserRepository;
import de.bentrm.datacat.properties.AppProperties;
import de.bentrm.datacat.properties.PropertyMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class AccountInitializer implements ApplicationRunner {

    @Autowired
    private PropertyMapper propertyMapper;

    @Autowired
    private AppProperties properties;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Running account initialization...");
        final var users = properties.getUsers();

        for (User value : userRepository.findAll()) {
            log.debug("User present: {}", value);
        }

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
