package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.auth.domain.User;
import de.bentrm.datacat.base.repository.UserRepository;
import de.bentrm.datacat.catalog.service.UserService;

import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;

@Service
@Validated
@Transactional(readOnly = true)
public class UserServiceImpl
        extends AbstractQueryServiceImpl<User, UserRepository>
        implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(Neo4jTemplate neo4jTemplate, UserRepository repository) {
        super(User.class, neo4jTemplate, repository);
        this.repository = repository;
    }

    @Override
    public User findByUsername(@NotNull String username) {
        return repository
                .findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("No account found."));
    }

    @Override
    public void updateName(@NotNull String username, @NotNull String firstname, @NotNull String lastname) {
        final User user = findByUsername(username);
        user.setFirstName(firstname);
        user.setLastName(lastname);
        repository.save(user);
    }

    @Override
    public void updateEmail(@NotNull String username, @NotNull String newEmail) {
        final User user = findByUsername(username);
        user.setEmail(newEmail);
        repository.save(user);
    }
}
