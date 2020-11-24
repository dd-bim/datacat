package de.bentrm.datacat.auth.service.dto;

import de.bentrm.datacat.auth.service.AccountStatus;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class AccountDto {
    String username;
    Instant created;
    Instant lastModified;
    boolean expired;
    boolean locked;
    boolean credentialsExpired;
    boolean emailConfirmed;
    boolean enabled;
    AccountStatus status;
    ProfileDto profile;
}
