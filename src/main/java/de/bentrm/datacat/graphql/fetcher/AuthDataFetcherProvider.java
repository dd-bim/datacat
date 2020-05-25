package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.auth.AuthenticationService;
import de.bentrm.datacat.auth.UserSession;
import de.bentrm.datacat.graphql.dto.LoginInput;
import de.bentrm.datacat.graphql.dto.SignupInput;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AuthDataFetcherProvider implements MutationDataFetcherProvider {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public Map<String, DataFetcher> getMutationDataFetchers() {
        return Map.ofEntries(
                Map.entry("signup", signup()),
                Map.entry("login", login())
        );
    }

    private DataFetcher<UserSession> signup() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            SignupInput dto = mapper.convertValue(input, SignupInput.class);
            return authenticationService.signup(dto);
        };
    }

    private DataFetcher<UserSession> login() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            LoginInput dto = mapper.convertValue(input, LoginInput.class);
            return authenticationService.login(dto.getUsername(), dto.getPassword());
        };
    }
}
