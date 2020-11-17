package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.auth.service.AdminService;
import de.bentrm.datacat.auth.specification.UserSpecification;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.AccountFilterInput;
import de.bentrm.datacat.graphql.dto.AccountStatusUpdateInput;
import de.bentrm.datacat.graphql.dto.AccountUpdateInput;
import de.bentrm.datacat.graphql.dto.SpecificationMapper;
import de.bentrm.datacat.service.dto.AccountDto;
import de.bentrm.datacat.service.dto.AccountUpdateDto;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingFieldSelectionSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
@Component
public class AdminFetchers implements QueryFetchers, MutationFetchers {

    public static final String IDENTIFIER = "username";
    public static final String INPUT = "input";

    @Autowired
    private SpecificationMapper specificationMapper;

    @Autowired
    private AdminService adminService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Map<String, DataFetcher> getQueryFetchers() {
        return Map.of(
                "account", fetchAccount(),
                "accounts", fetchAccounts()
        );
    }

    @Override
    public Map<String, DataFetcher> getMutationFetchers() {
        return Map.of(
                "updateAccount", updateAccount(),
                "updateAccountStatus", updateAccountStatus(),
                "lockAccount", lockAccount(),
                "unlockAccount", unlockAccount(),
                "requestEmailConfirmation", requestEmailConfirmation()
        );
    }

    private DataFetcher<Optional<AccountDto>> fetchAccount() {
        return env -> {
            String username = env.getArgument(IDENTIFIER);
            return adminService.findAccount(username);
        };
    }

    private DataFetcher<Connection<AccountDto>> fetchAccounts() {
        return env -> {
            final DataFetchingFieldSelectionSet selectionSet = env.getSelectionSet();
            AccountFilterInput filter = toValue(env.getArgument(INPUT), AccountFilterInput.class, AccountFilterInput::new);
            UserSpecification specification = specificationMapper.toSpecification(filter);

            if (selectionSet.containsAnyOf("nodes/*", "pageInfo/*")) {
                final Page<AccountDto> page = adminService.findAccounts(specification);
                return Connection.of(page);
            } else {
                log.info("Retrieving empty connection only querying count.");
                return Connection.empty(adminService.countAccounts(specification));
            }
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
            String username = env.getArgument(IDENTIFIER);
            return adminService.lockAccount(username);
        };
    }

    private DataFetcher<Optional<AccountDto>> unlockAccount() {
        return env -> {
            String username = env.getArgument(IDENTIFIER);
            return adminService.unlockAccount(username);
        };
    }

    private DataFetcher<Optional<AccountDto>> requestEmailConfirmation() {
        return env -> {
            String username = env.getArgument(IDENTIFIER);
            return adminService.requestEmailConfirmation(username);
        };
    }

    private <T, R> R toValue(T input, Class<R> targetType, Supplier<R> supplier) {
        final R value = toValue(input, targetType);
        return value != null ? value : supplier.get();
    }

    private <T, R> R toValue(T input, Class<R> targetType) {
        return objectMapper.convertValue(input, targetType);
    }
}
