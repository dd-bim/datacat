package de.bentrm.datacat.auth.service;

import de.bentrm.datacat.auth.domain.EmailConfirmationRequest;
import de.bentrm.datacat.auth.domain.Role;
import de.bentrm.datacat.auth.domain.User;
import de.bentrm.datacat.auth.specification.UserSpecification;
import de.bentrm.datacat.base.repository.EmailConfirmationRepository;
import de.bentrm.datacat.base.repository.UserRepository;
import de.bentrm.datacat.catalog.service.value.ValueMapper;
import de.bentrm.datacat.service.dto.AccountDto;
import de.bentrm.datacat.service.dto.AccountUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Validated
@Transactional
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailConfirmationRepository emailConfirmationRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ValueMapper valueMapper;

    @Override
    public AccountDto updateAccount(AccountUpdateDto dto) {
        User user = userRepository
                .findByUsername(dto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("No account found."));
        valueMapper.setProperties(dto, user);
        user = userRepository.save(user);
        return valueMapper.toAccountDto(user);
    }

    @Override
    public AccountDto updateAccountStatus(String username, AccountStatus newStatus) {
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("No account found."));

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
        final Optional<User> result = userRepository.findByUsername(username);
        if (result.isPresent()) {
            User user = result.get();
            user.setLocked(locked);
            user = userRepository.save(user);
            final AccountDto accountDto = valueMapper.toAccountDto(user);
            return Optional.of(accountDto);
        }
        return Optional.empty();
    }

    @Override
    public Optional<AccountDto> requestEmailConfirmation(@NotBlank String username) {
        return userRepository.findByUsername(username)
                .map(user -> {
                    EmailConfirmationRequest confirmation = EmailConfirmationRequest.of(user);
                    confirmation = emailConfirmationRepository.save(confirmation);
                    emailService.sendEmailConfirmation(confirmation);
                    return user;
                })
                .map(valueMapper::toAccountDto);
    }

    @Override
    public Optional<AccountDto> findAccount(String username) {
        return userRepository
                .findByUsername(username)
                .map(user -> valueMapper.toAccountDto(user));
    }

    @Override
    public long countAccounts(UserSpecification specification) {
        return userRepository.count(specification);
    }

    @Override
    public Page<AccountDto> findAccounts(UserSpecification specification) {
        final Page<User> content = userRepository.findAll(specification);
        final List<AccountDto> newContent = content.getContent().stream()
                .map(user -> valueMapper.toAccountDto(user))
                .collect(Collectors.toList());
        return PageableExecutionUtils.getPage(newContent, content.getPageable(), content::getTotalElements);
    }
}
