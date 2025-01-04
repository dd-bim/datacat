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
import de.bentrm.datacat.catalog.service.impl.AbstractQueryServiceImpl;
import de.bentrm.datacat.catalog.service.value.ValueMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.neo4j.core.Neo4jTemplate;

@Service
@Validated
@Transactional(readOnly = true)
public class AdminServiceImpl extends AbstractQueryServiceImpl<User, UserRepository> implements AdminService {

    private final EmailConfirmationRepository emailConfirmationRepository;
    private final EmailService emailService;
    private final ValueMapper valueMapper;

    public AdminServiceImpl(Neo4jTemplate neo4jTemplate,
                            UserRepository repository,
                            EmailConfirmationRepository emailConfirmationRepository,
                            EmailService emailService,
                            ValueMapper valueMapper) {
        super(User.class, neo4jTemplate, repository);
        this.emailConfirmationRepository = emailConfirmationRepository;
        this.emailService = emailService;
        this.valueMapper = valueMapper;
    }


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
        return getRepository()
                .findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("No account found."));
    }

    @Transactional
    @Override
    public void updateName(@NotNull String username, @NotNull String firstname, @NotNull String lastname) {
        final User user = findByUsername(username);
        user.setFirstName(firstname);
        user.setLastName(lastname);
        getRepository().save(user);
    }

    @Transactional
    @Override
    public void updateEmail(@NotNull String username, @NotNull String newEmail) {
        final User user = findByUsername(username);
        user.setEmail(newEmail);
        getRepository().save(user);
    }

    @Transactional
    @Override
    public AccountDto updateAccountStatus(String username, AccountStatus newStatus) {
        User user = findByUsername(username);

        Set<Role> newRoles = switch (newStatus) {
            case Admin -> Set.of(Role.ADMIN, Role.USER, Role.READONLY);
            case Verified -> Set.of(Role.USER, Role.READONLY);
            case Unverified -> Set.of(Role.READONLY);
        };
        user.setRoles(newRoles);
        user = getRepository().save(user);

        return valueMapper.toAccountDto(user);
    }

    @Transactional
    @Override
    public Optional<AccountDto> lockAccount(String username) {
        return setAccountLock(username, true);
    }

    @Transactional
    @Override
    public Optional<AccountDto> unlockAccount(String username) {
        return setAccountLock(username, false);
    }

    private Optional<AccountDto> setAccountLock(String username, boolean locked) {
        User user = findByUsername(username);
        user.setLocked(locked);
        user = getRepository().save(user);
        final AccountDto accountDto = valueMapper.toAccountDto(user);
        return Optional.of(accountDto);
    }

    @Transactional
    @Override
    public Optional<AccountDto> requestEmailConfirmation(@NotBlank String username) {
        final User user = findByUsername(username);
        EmailConfirmationRequest confirmation = EmailConfirmationRequest.of(user);
        confirmation = emailConfirmationRepository.save(confirmation);
        emailService.sendEmailConfirmation(confirmation);
        final AccountDto accountDto = valueMapper.toAccountDto(user);
        return Optional.of(accountDto);
    }

    @Transactional
    @Override
    public Optional<AccountDto> deleteAccount(@NotBlank String username) {
        final User user = findByUsername(username);
        getRepository().delete(user);
        final AccountDto accountDto = valueMapper.toAccountDto(user);
        return Optional.of(accountDto);
    }

    @Override
    public Optional<AccountDto> findAccount(String username) {
        return getRepository()
                .findByUsername(username)
                .map(valueMapper::toAccountDto);
    }

    @Override
    public long countAccounts(@NotNull UserSpecification specification) {
        String query;
        if (specification.getFilters().isEmpty()) {
            query = "MATCH (n:User) RETURN count(n)";
        } else {
            String whereClause = "WHERE " + String.join(" AND ", specification.getFilters());
            query = "MATCH (n:User) " + whereClause + " RETURN count(n)";
        }
        return neo4jTemplate.count(query);
    }

    @Override
    public Page<AccountDto> findAccounts(@NotNull UserSpecification specification) {
        Collection<User> users;
        Pageable pageable;
        final long count = countAccounts(specification);

        final Optional<Pageable> paged = specification.getPageable();
        if (paged.isPresent()) {
            pageable = paged.get();
            if (pageable.getSort().isUnsorted()) {
                users = neo4jTemplate.findAll(getQuery(specification, pageable), User.class);
            } else {
                final Sort sort = pageable.getSort();
                final Sort.Direction direction = sort.get().findFirst().map(Sort.Order::getDirection).get();
                final String[] properties = sort.get().map(Sort.Order::getProperty).toArray(String[]::new);
                users = neo4jTemplate.findAll(getQuery(specification, pageable, direction, properties), User.class);
            }
        } else {
            pageable = PageRequest.of(0, (int) Math.max(count, 10));
            users = neo4jTemplate.findAll(getQuery(specification, pageable), User.class);
        }

        final List<AccountDto> newContent = users.stream()
                .map(valueMapper::toAccountDto)
                .collect(Collectors.toList());
        return PageableExecutionUtils.getPage(newContent, pageable, () -> count);
    }

    
    public String getQuery(UserSpecification specification, Pageable pageable) {
        return (getQuery(specification, pageable, null, null));
    }

        public String getQuery(UserSpecification specification, Pageable pageable, Sort.Direction direction,
            String[] properties) {
        String query;
        String sort = "";
        if (direction != null && properties != null) {
            List<String> prefixedProperties = Arrays.stream(properties).map(property -> "n." + property)
                    .collect(Collectors.toList());
            sort = " ORDER BY " + String.join(", ", prefixedProperties) + " " + direction.name();
        } 

        if (specification.getFilters().isEmpty()) {
            query = "MATCH (n:User)" + sort + " RETURN n";
        } else {
            String whereClause = "WHERE " + String.join(" AND ", specification.getFilters());
            query = "MATCH (n:User) " + whereClause + sort +" RETURN n";
        }
        query = query + " SKIP " + pageable.getOffset() + " LIMIT " + pageable.getPageSize();
        return query;
    }
}
