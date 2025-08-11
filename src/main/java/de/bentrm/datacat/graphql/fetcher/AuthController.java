package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.auth.service.*;
import de.bentrm.datacat.auth.service.dto.ProfileDto;
import de.bentrm.datacat.graphql.dto.LoginInput;
import de.bentrm.datacat.graphql.dto.ProfileUpdateInput;
import de.bentrm.datacat.graphql.dto.SignupInput;
import de.bentrm.datacat.graphql.dto.SpecificationMapper;
import graphql.GraphQLError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@Controller
public class AuthController {

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

}
