package de.bentrm.datacat.service;

import de.bentrm.datacat.repository.UserSpecification;
import de.bentrm.datacat.service.dto.AccountDto;
import de.bentrm.datacat.service.dto.AccountUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface AdminService {

    @PreAuthorize("hasRole('ADMIN')")
    AccountDto updateAccount(@Valid AccountUpdateDto dto);

    @PreAuthorize("hasRole('ADMIN')")
    AccountDto updateAccountStatus(@NotNull String username, @NotNull AccountStatus newStatus);

    @PreAuthorize("hasRole('ADMIN')")
    Optional<AccountDto> lockAccount(@NotBlank String username);

    @PreAuthorize("hasRole('ADMIN')")
    Optional<AccountDto> unlockAccount(@NotBlank String username);

    @PreAuthorize("hasRole('ADMIN')")
    Optional<AccountDto> requestEmailConfirmation(@NotBlank String username);

    @PreAuthorize("hasRole('ADMIN')")
    Optional<AccountDto> findAccount(@NotBlank String username);

    @PreAuthorize("hasRole('ADMIN')")
    long countAccounts(@NotNull UserSpecification specification);

    @PreAuthorize("hasRole('ADMIN')")
    Page<AccountDto> findAccounts(@NotNull UserSpecification specification);

}
