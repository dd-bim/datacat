package de.bentrm.datacat.auth.service.impl;

import de.bentrm.datacat.auth.domain.EmailConfirmationRequest;
import de.bentrm.datacat.auth.domain.Role;
import de.bentrm.datacat.auth.domain.User;
import de.bentrm.datacat.auth.service.AccountStatus;
import de.bentrm.datacat.auth.service.AdminService;
import de.bentrm.datacat.auth.service.EmailService;
import de.bentrm.datacat.auth.service.dto.AccountDto;
import de.bentrm.datacat.auth.service.dto.AccountUpdateDto;
import de.bentrm.datacat.auth.specification.UserSpecification;
import de.bentrm.datacat.base.repository.EmailConfirmationRepository;
import de.bentrm.datacat.base.repository.UserRepository;
import de.bentrm.datacat.catalog.service.value.ValueMapper;
import org.neo4j.ogm.cypher.query.Pagination;
import org.neo4j.ogm.cypher.query.SortOrder;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Validated
@Transactional(readOnly = true)
public class AdminServiceImpl implements AdminService {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailConfirmationRepository emailConfirmationRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ValueMapper valueMapper;

    @Transactional
    @Override
    public AccountDto updateAccount(AccountUpdateDto dto) {
        updateName(dto.getUsername(), dto.getFirstName(), dto.getLastName());
        updateEmail(dto.getUsername(), dto.getEmail());
        final User user = findByUsername(dto.getUsername());
        return valueMapper.toAccountDto(user);
    }

    @Override
    public User findByUsername(@NotNull String username) {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("No account found."));
    }

    @Override
    public void updateName(@NotNull String username, @NotNull String firstname, @NotNull String lastname) {
        final User user = findByUsername(username);
        user.setFirstName(firstname);
        user.setLastName(lastname);
        userRepository.save(user);
    }

    @Override
    public void updateEmail(@NotNull String username, @NotNull String newEmail) {
        final User user = findByUsername(username);
        user.setEmail(newEmail);
        userRepository.save(user);
    }

    @Override
    public AccountDto updateAccountStatus(String username, AccountStatus newStatus) {
        User user = findByUsername(username);

        Set<Role> newRoles = switch (newStatus) {
            case Admin -> Set.of(Role.ADMIN, Role.USER, Role.READONLY);
            case Verified -> Set.of(Role.USER, Role.READONLY);
            case Unverified -> Set.of(Role.READONLY);
        };
        user.setRoles(newRoles);
        user = userRepository.save(user);

        return valueMapper.toAccountDto(user);
    }

    @Override
    public Optional<AccountDto> lockAccount(String username) {
        return setAccountLock(username, true);
    }

    @Override
    public Optional<AccountDto> unlockAccount(String username) {
        return setAccountLock(username, false);
    }

    private Optional<AccountDto> setAccountLock(String username, boolean locked) {
        User user = findByUsername(username);
        user.setLocked(locked);
        user = userRepository.save(user);
        final AccountDto accountDto = valueMapper.toAccountDto(user);
        return Optional.of(accountDto);
    }

    @Override
    public Optional<AccountDto> requestEmailConfirmation(@NotBlank String username) {
        final User user = findByUsername(username);
        EmailConfirmationRequest confirmation = EmailConfirmationRequest.of(user);
        confirmation = emailConfirmationRepository.save(confirmation);
        emailService.sendEmailConfirmation(confirmation);
        final AccountDto accountDto = valueMapper.toAccountDto(user);
        return Optional.of(accountDto);
    }

    @Override
    public Optional<AccountDto> findAccount(String username) {
        return userRepository
                .findByUsername(username)
                .map(user -> valueMapper.toAccountDto(user));
    }

    @Override
    public long countAccounts(UserSpecification specification) {
        final Session session = sessionFactory.openSession();
        return session.count(User.class, specification.getFilters());
    }

    @Override
    public Page<AccountDto> findAccounts(UserSpecification specification) {
        Collection<User> users;
        Pageable pageable;
        final long count = countAccounts(specification);
        final Session session = sessionFactory.openSession();

        final Optional<Pageable> paged = specification.getPageable();
        if (paged.isPresent()) {
            pageable = paged.get();
            final Pagination pagination = new Pagination(pageable.getPageNumber(), pageable.getPageSize());

            if (pageable.getSort().isUnsorted()) {
                users = session.loadAll(User.class, specification.getFilters(), pagination);
            } else {
                final Sort sort = pageable.getSort();
                final Sort.Direction direction = sort.get().findFirst().map(Sort.Order::getDirection).get();
                final String[] properties = sort.get().map(Sort.Order::getProperty).toArray(String[]::new);
                final SortOrder sortOrder = new SortOrder(SortOrder.Direction.valueOf(direction.name()), properties);
                users = session.loadAll(User.class, specification.getFilters(), sortOrder, pagination);
            }
        } else {
            pageable = PageRequest.of(0, (int) Math.max(count, 10));
            users = session.loadAll(User.class, specification.getFilters());
        }

        final List<AccountDto> newContent = users.stream()
                .map(user -> valueMapper.toAccountDto(user))
                .collect(Collectors.toList());
        return PageableExecutionUtils.getPage(newContent, pageable, () -> count);
    }
}
