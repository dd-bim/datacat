package de.bentrm.datacat.service;

import de.bentrm.datacat.graphql.dto.SignupInput;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public interface AuthenticationService {

    @PreAuthorize("isAnonymous()")
    void signup(@NotNull @Valid SignupInput signupInput);

    @PreAuthorize("isAnonymous()")
    void fulfillEmailConfirmationRequest(@NotNull String emailConfirmationToken);

    @PreAuthorize("isAnonymous()")
    String login(@NotBlank String username, @NotBlank String password);

    void login(@NotBlank String jwtToken);

}
