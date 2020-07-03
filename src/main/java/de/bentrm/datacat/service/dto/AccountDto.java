package de.bentrm.datacat.service.dto;

import de.bentrm.datacat.service.AccountStatus;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AccountDto {
    String username;
    String firstName;
    String lastName;
    String email;
    String organization;
    boolean expired;
    boolean locked;
    boolean credentialsExpired;
    boolean enabled;
    AccountStatus status;
}
