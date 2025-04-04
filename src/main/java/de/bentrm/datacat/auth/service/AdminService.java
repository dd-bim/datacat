package de.bentrm.datacat.auth.service;

import de.bentrm.datacat.auth.domain.User;
import de.bentrm.datacat.auth.service.dto.AccountDto;
import de.bentrm.datacat.auth.service.dto.AccountUpdateDto;
import de.bentrm.datacat.auth.specification.UserSpecification;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;

public interface AdminService {

    @PreAuthorize("hasRole('ADMIN')")
    AccountDto updateAccount(@Valid AccountUpdateDto dto);

    @PreAuthorize("hasRole('ADMIN')")
    User findByUsername(@NotNull String id);

    @PreAuthorize("hasRole('ADMIN')")
    void updateName(@NotNull String username, @NotNull String firstname, @NotNull String lastname);

    @PreAuthorize("hasRole('ADMIN')")
    void updateEmail(@NotNull String username, @NotNull String newEmail);

    @PreAuthorize("hasRole('ADMIN')")
    AccountDto updateAccountStatus(@NotNull String username, @NotNull AccountStatus newStatus);

    @PreAuthorize("hasRole('ADMIN')")
    Optional<AccountDto> lockAccount(@NotBlank String username);

    @PreAuthorize("hasRole('ADMIN')")
    Optional<AccountDto> unlockAccount(@NotBlank String username);

    @PreAuthorize("hasRole('ADMIN')")
    Optional<AccountDto> requestEmailConfirmation(@NotBlank String username);

    @PreAuthorize("hasRole('ADMIN')")
    Optional<AccountDto> deleteAccount(@NotBlank String username);

    @PreAuthorize("hasRole('ADMIN')")
    Optional<AccountDto> findAccount(@NotBlank String username);

    @PreAuthorize("hasRole('ADMIN')")
    Long countAccounts(@NotNull UserSpecification specification);

    @PreAuthorize("hasRole('ADMIN')")
    Page<AccountDto> findAccounts(@NotNull UserSpecification specification);

}
