package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.auth.service.*;
import de.bentrm.datacat.auth.service.dto.ProfileDto;
import de.bentrm.datacat.graphql.dto.LoginInput;
import de.bentrm.datacat.graphql.dto.ProfileUpdateInput;
import de.bentrm.datacat.graphql.dto.SignupInput;
import de.bentrm.datacat.graphql.dto.SpecificationMapper;
import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.language.SourceLocation;
import graphql.schema.DataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.graphql.execution.ErrorType;

import java.util.List;
import java.util.Map;

@Slf4j
// @Component
// public class AuthFetchers implements QueryFetchers, MutationFetchers {
@Controller
public class AuthFetchers {

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
        return GraphQLError.newError().errorType(ErrorType.BAD_REQUEST).message(e.getMessage()).build();
    }

    // @Override
    // public Map<String, DataFetcher> getQueryFetchers() {
    //     return Map.ofEntries(
    //             Map.entry("profile", profile())
    //     );
    // }

    // @Override
    // public Map<String, DataFetcher> getMutationFetchers() {
    //     return Map.ofEntries(
    //             Map.entry("signup", signup()),
    //             Map.entry("confirm", confirm()),
    //             // Map.entry("login", login()),
    //             Map.entry("updateProfile", updateProfile())
    //     );
    // }

    // private DataFetcher<Boolean> signup() {
    //     return environment -> {
    //         Map<String, Object> input = environment.getArgument("input");
    //         SignupInput dto = mapper.convertValue(input, SignupInput.class);
    //         authenticationService.signup(dto);
    //         return true;
    //     };
    // }

    // private DataFetcher<Boolean> confirm() {
    //     return env -> {
    //         final String token = env.getArgument("token");
    //         authenticationService.fulfillEmailConfirmationRequest(token);
    //         return true;
    //     };
    // }

    // private DataFetcher<String> login() {
    //     return environment -> {
    //         Map<String, Object> input = environment.getArgument("input");
    //         LoginInput dto = mapper.convertValue(input, LoginInput.class);
    //         return authenticationService.login(dto.getUsername(), dto.getPassword());
    //     };
    // }

    @QueryMapping
    public ProfileDto profile() {
        return profileService.getProfile();
    }

    @MutationMapping
    public Boolean signup(@Argument SignupInput input) {
        authenticationService.signup(input);
        return true;
    }

    @MutationMapping
    public Boolean confirm(@Argument String token) {
        authenticationService.fulfillEmailConfirmationRequest(token);
        return true;
    }

    @MutationMapping
    public String login(@Argument LoginInput input) {
        return authenticationService.login(input.getUsername(), input.getPassword());
    }

    @MutationMapping
    public ProfileDto updateProfile(@Argument ProfileUpdateInput input) {
        return profileService.updateAccount(specificationMapper.toDto(input));
    }

    // private DataFetcher<ProfileDto> profile() {
    //     return env -> profileService.getProfile();
    // }

    // private DataFetcher<ProfileDto> updateProfile() {
    //     return env -> {
    //         Map<String, Object> input = env.getArgument("input");
    //         ProfileUpdateInput dto = mapper.convertValue(input, ProfileUpdateInput.class);
    //         return profileService.updateAccount(specificationMapper.toDto(dto));
    //     };
    // }
}
