package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.auth.service.AdminService;
import de.bentrm.datacat.auth.service.dto.AccountDto;
import de.bentrm.datacat.auth.service.dto.AccountUpdateDto;
import de.bentrm.datacat.auth.specification.UserSpecification;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.AccountFilterInput;
import de.bentrm.datacat.graphql.dto.AccountStatusUpdateInput;
import de.bentrm.datacat.graphql.dto.AccountUpdateInput;
import de.bentrm.datacat.graphql.dto.SpecificationMapper;
import graphql.schema.DataFetchingFieldSelectionSet;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Slf4j
@Controller
public class AdminController {

    @Autowired
    private SpecificationMapper specificationMapper;

    @Autowired
    private AdminService adminService;

    @QueryMapping
    public Connection<AccountDto> findAccounts(@Argument AccountFilterInput input, DataFetchingFieldSelectionSet environment) {
        if (input == null) input = new AccountFilterInput();
 
        UserSpecification specification = SpecificationMapper.INSTANCE.toSpecification(input);
        if (environment.containsAnyOf("nodes/*", "pageInfo/*")) {
            Page<AccountDto> page = adminService.findAccounts(specification);
            return Connection.of(page);
        } else {
            long count = adminService.countAccounts(specification);
            return Connection.empty(count);
        }
    }

    @QueryMapping
    public Optional<AccountDto> account(@Argument String username) {
        return adminService.findAccount(username);
    }

    @MutationMapping
    public AccountDto updateAccount(@Argument AccountUpdateInput input) {
        AccountUpdateDto accountDto = specificationMapper.toDto(input);
        return adminService.updateAccount(accountDto);
    }

    @MutationMapping
    public AccountDto updateAccountStatus(@Argument AccountStatusUpdateInput input) {
        return adminService.updateAccountStatus(input.getUsername(), input.getStatus());
    }

    @MutationMapping
    public Optional<AccountDto> lockAccount(@Argument String username) {
        return adminService.lockAccount(username);
    }

    @MutationMapping
    public Optional<AccountDto> unlockAccount(@Argument String username) {
        return adminService.unlockAccount(username);
    }

    @MutationMapping
    public Optional<AccountDto> requestEmailConfirmation(@Argument String username) {
        return adminService.requestEmailConfirmation(username);
    }

    @MutationMapping
    public Optional<AccountDto> deleteAccount(@Argument String username) {
        return adminService.deleteAccount(username);
    }

}
