package de.bentrm.datacat;

import de.bentrm.datacat.auth.PasswordGenerator;
import de.bentrm.datacat.domain.User;
import de.bentrm.datacat.properties.ApplicationProperties;
import de.bentrm.datacat.properties.PropertyMapper;
import de.bentrm.datacat.properties.UserProperties;
import de.bentrm.datacat.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AccountInitializer implements ApplicationRunner {

    @Autowired
    private PropertyMapper propertyMapper;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) {
        final List<UserProperties> users = applicationProperties.getUsers();

        for (var properties : users) {
            final String username = properties.getUsername();

            if (userRepository.findByUsername(username).isPresent()) {
                log.info("Account with username {} found... skipping account creation.", username);
                continue;
            }

            log.info("Adding new account {}...", username);
            final User user = propertyMapper.toUser(properties);
            String password = properties.getPassword();
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
