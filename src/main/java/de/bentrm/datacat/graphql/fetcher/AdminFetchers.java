package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.auth.service.AdminService;
import de.bentrm.datacat.auth.service.dto.AccountDto;
import de.bentrm.datacat.auth.service.dto.AccountUpdateDto;
import de.bentrm.datacat.auth.specification.UserSpecification;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.AccountFilterInput;
import de.bentrm.datacat.graphql.dto.AccountStatusUpdateInput;
import de.bentrm.datacat.graphql.dto.AccountUpdateInput;
import de.bentrm.datacat.graphql.dto.SpecificationMapper;
import graphql.schema.DataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class AdminFetchers implements QueryFetchers, MutationFetchers {

    public static final String USERNAME = "username";
    public static final String INPUT = "input";

    private final SpecificationMapper specificationMapper;
    private final AdminService adminService;

//    private final DataFetcher<Optional<AccountDto>> fetchOne;
    private final DataFetcher<Connection<AccountDto>> fetchAllAccounts;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public AdminFetchers(SpecificationMapper specificationMapper, AdminService adminService) {
        this.specificationMapper = specificationMapper;
        this.adminService = adminService;

        this.fetchAllAccounts = environment -> {
            Map<String, Object> input = environment.getArgument("input");
            AccountFilterInput filter = objectMapper.convertValue(input, AccountFilterInput.class);
            if (filter == null) filter = new AccountFilterInput();

            UserSpecification specification = SpecificationMapper.INSTANCE.toSpecification(filter);
            if (environment.getSelectionSet().containsAnyOf("nodes/*", "pageInfo/*")) {
                Page<AccountDto> page = adminService.findAccounts(specification);
                return Connection.of(page);
            } else {
                long count = adminService.countAccounts(specification);
                return Connection.empty(count);
            }
        };
    }

    @Override
    public Map<String, DataFetcher> getQueryFetchers() {
        return Map.of(
                "account", fetchAccount(),
                "findAccounts", fetchAllAccounts
        );
    }

    @Override
    public Map<String, DataFetcher> getMutationFetchers() {
        return Map.of(
                "updateAccount", updateAccount(),
                "updateAccountStatus", updateAccountStatus(),
                "lockAccount", lockAccount(),
                "unlockAccount", unlockAccount(),
                "requestEmailConfirmation", requestEmailConfirmation(),
                "deleteAccount", deleteAccount()
        );
    }

    private DataFetcher<Optional<AccountDto>> fetchAccount() {
        return env -> {
            String username = env.getArgument(USERNAME);
            return adminService.findAccount(username);
        };
    }

    private DataFetcher<AccountDto> updateAccount() {
        return env -> {
            final AccountUpdateInput dto = toValue(env.getArgument(INPUT), AccountUpdateInput.class);
            AccountUpdateDto accountDto = specificationMapper.toDto(dto);
            return adminService.updateAccount(accountDto);
        };
    }

    private DataFetcher<AccountDto> updateAccountStatus() {
        return env -> {
            final AccountStatusUpdateInput dto = toValue(env.getArgument(INPUT), AccountStatusUpdateInput.class);
            return adminService.updateAccountStatus(dto.getUsername(), dto.getStatus());
        };
    }

    private DataFetcher<Optional<AccountDto>> lockAccount() {
        return env -> {
            String username = env.getArgument(USERNAME);
            return adminService.lockAccount(username);
        };
    }

    private DataFetcher<Optional<AccountDto>> unlockAccount() {
        return env -> {
            String username = env.getArgument(USERNAME);
            return adminService.unlockAccount(username);
        };
    }

    private DataFetcher<Optional<AccountDto>> requestEmailConfirmation() {
        return env -> {
            String username = env.getArgument(USERNAME);
            return adminService.requestEmailConfirmation(username);
        };
    }

    private DataFetcher<Optional<AccountDto>> deleteAccount() {
        return env -> {
            String username = env.getArgument(USERNAME);
            return adminService.deleteAccount(username);
        };
    }

    private <T, R> R toValue(T input, Class<R> targetType) {
        return objectMapper.convertValue(input, targetType);
    }
}
