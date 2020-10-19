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
        final var users = properties.getUsers();

        for (var entry : users.entrySet()) {
            final String username = entry.getKey();
            final var userProperties = entry.getValue();

            if (userRepository.findByUsername(username).isPresent()) {
                log.info("Account with username {} found... skipping account creation.", username);
                continue;
            }

            log.info("Adding new account {}...", username);
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
