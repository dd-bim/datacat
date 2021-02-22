package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.auth.domain.User;
import de.bentrm.datacat.base.repository.UserRepository;
import de.bentrm.datacat.catalog.service.UserService;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Service
@Validated
@Transactional(readOnly = true)
public class UserServiceImpl
        extends AbstractQueryServiceImpl<User, UserRepository>
        implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(SessionFactory sessionFactory, UserRepository repository) {
        super(User.class, sessionFactory, repository);
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
