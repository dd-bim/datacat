package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.auth.UserSession;
import de.bentrm.datacat.graphql.dto.LoginInput;
import de.bentrm.datacat.graphql.dto.SignupInput;
import de.bentrm.datacat.service.UserService;
import graphql.schema.DataFetcher;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AuthDataFetcherProvider implements MutationDataFetcherProvider {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private Logger logger;

    @Autowired
    private UserService userService;

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
            return userService.signup(dto);
        };
    }

    private DataFetcher<UserSession> login() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            LoginInput dto = mapper.convertValue(input, LoginInput.class);
            logger.debug("Called {}", dto);
            return userService.login(dto.getUsername(), dto.getPassword());
        };
    }
}
