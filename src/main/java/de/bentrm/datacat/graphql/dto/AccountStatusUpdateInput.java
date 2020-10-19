package de.bentrm.datacat.graphql.dto;

import de.bentrm.datacat.auth.service.AccountStatus;
import lombok.Data;

@Data
public class AccountStatusUpdateInput {
    private String username;
    private AccountStatus status;
}
