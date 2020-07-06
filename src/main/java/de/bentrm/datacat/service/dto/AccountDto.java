package de.bentrm.datacat.service.dto;

import de.bentrm.datacat.service.AccountStatus;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AccountDto {
    ProfileDto profile;
    boolean expired;
    boolean locked;
    boolean credentialsExpired;
    boolean enabled;
    AccountStatus status;
}
