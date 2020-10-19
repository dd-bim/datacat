package de.bentrm.datacat.graphql.fetchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.auth.service.*;
import de.bentrm.datacat.graphql.dto.LoginInput;
import de.bentrm.datacat.graphql.dto.ProfileUpdateInput;
import de.bentrm.datacat.graphql.dto.SignupInput;
import de.bentrm.datacat.graphql.dto.SpecificationMapper;
import de.bentrm.datacat.service.dto.ProfileDto;
import graphql.GraphQLError;
import graphql.kickstart.spring.error.ThrowableGraphQLError;
import graphql.schema.DataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@Slf4j
@Component
public class AuthFetchers implements QueryFetchers, MutationFetchers {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private SpecificationMapper specificationMapper;

    @ExceptionHandler({
            UsernameTakenException.class,
            EmailTakenException.class,
            EmailUnconfirmedException.class,
            BadCredentialsException.class
    })
    public GraphQLError handleException(Throwable e) {
        return new ThrowableGraphQLError(e);
    }

    @Override
    public Map<String, DataFetcher> getQueryFetchers() {
        return Map.ofEntries(
                Map.entry("profile", profile())
        );
    }

    @Override
    public Map<String, DataFetcher> getMutationFetchers() {
        return Map.ofEntries(
                Map.entry("signup", signup()),
                Map.entry("confirm", confirm()),
                Map.entry("login", login()),
                Map.entry("updateProfile", updateProfile())
        );
    }

    private DataFetcher<Boolean> signup() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            SignupInput dto = mapper.convertValue(input, SignupInput.class);
            authenticationService.signup(dto);
            return true;
        };
    }

    private DataFetcher<Boolean> confirm() {
        return env -> {
            final String token = env.getArgument("token");
            authenticationService.fulfillEmailConfirmationRequest(token);
            return true;
        };
    }

    private DataFetcher<String> login() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            LoginInput dto = mapper.convertValue(input, LoginInput.class);
            return authenticationService.login(dto.getUsername(), dto.getPassword());
        };
    }

    private DataFetcher<ProfileDto> profile() {
        return env -> profileService.getProfile();
    }

    private DataFetcher<ProfileDto> updateProfile() {
        return env -> {
            Map<String, Object> input = env.getArgument("input");
            ProfileUpdateInput dto = mapper.convertValue(input, ProfileUpdateInput.class);
            return profileService.updateAccount(specificationMapper.toDto(dto));
        };
    }
}
