package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.EmailConfirmationRequest;
import de.bentrm.datacat.domain.Roles;
import de.bentrm.datacat.domain.User;
import de.bentrm.datacat.repository.EmailConfirmationRepository;
import de.bentrm.datacat.repository.UserRepository;
import de.bentrm.datacat.repository.UserSpecification;
import de.bentrm.datacat.service.AccountStatus;
import de.bentrm.datacat.service.AdminService;
import de.bentrm.datacat.service.EmailService;
import de.bentrm.datacat.service.dto.AccountDto;
import de.bentrm.datacat.service.dto.AccountUpdateDto;
import de.bentrm.datacat.service.dto.DtoMapper;
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
    private DtoMapper dtoMapper;

    @Override
    public AccountDto updateAccount(AccountUpdateDto dto) {
        User user = userRepository
                .findByUsername(dto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("No account found."));
        dtoMapper.setProperties(dto, user);
        user = userRepository.save(user);
        return dtoMapper.toAccountDto(user);
    }

    @Override
    public AccountDto updateAccountStatus(String username, AccountStatus newStatus) {
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("No account found."));

        Set<Roles> newRoles = switch (newStatus) {
            case Admin -> Set.of(Roles.ROLE_ADMIN, Roles.ROLE_USER, Roles.ROLE_READONLY);
            case Verified -> Set.of(Roles.ROLE_USER, Roles.ROLE_READONLY);
            case Unverified -> Set.of(Roles.ROLE_READONLY);
        };
        user.setRoles(newRoles);
        user = userRepository.save(user);

        return dtoMapper.toAccountDto(user);
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
            final AccountDto accountDto = dtoMapper.toAccountDto(user);
            return Optional.of(accountDto);
        }
        return Optional.empty();
    }

    @Override
    public void requestEmailConfirmation(@NotBlank String username) {
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("No account found."));
        EmailConfirmationRequest confirmation = EmailConfirmationRequest.of(user);
        confirmation = emailConfirmationRepository.save(confirmation);
        emailService.sendEmailConfirmation(confirmation);
    }

    @Override
    public Optional<AccountDto> findAccount(String username) {
        return userRepository
                .findByUsername(username)
                .map(user -> dtoMapper.toAccountDto(user));
    }

    @Override
    public long countAccounts(UserSpecification specification) {
        return userRepository.count(specification);
    }

    @Override
    public Page<AccountDto> findAccounts(UserSpecification specification) {
        final Page<User> content = userRepository.findAll(specification);
        final List<AccountDto> newContent = content.getContent().stream()
                .map(user -> dtoMapper.toAccountDto(user))
                .collect(Collectors.toList());
        return PageableExecutionUtils.getPage(newContent, content.getPageable(), content::getTotalElements);
    }
}
