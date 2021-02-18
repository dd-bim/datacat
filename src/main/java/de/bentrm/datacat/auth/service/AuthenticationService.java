package de.bentrm.datacat.auth.service;

import de.bentrm.datacat.graphql.dto.SignupInput;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public interface AuthenticationService {

    /**
     * Allows to create new user entities from the given input.
     * Typically, after create the accounts email needs to be verified before
     * it can be used to login.
     * @param signupInput The new accounts properties.
     */
    @PreAuthorize("isAnonymous()")
    void signup(@NotNull @Valid SignupInput signupInput);

    /**
     * Fulfills outstanding account confirmations. The confirmation may not be
     * expired. A confirmation request can only be used one time.
     * @param emailConfirmationToken The text token that must equal the token of the outstanding request.
     */
    @PreAuthorize("isAnonymous()")
    void fulfillEmailConfirmationRequest(@NotNull String emailConfirmationToken);

    /**
     * Used to login by username and password.
     * @param username The username.
     * @param password The password.
     * @return the authentication token.
     */
    @PreAuthorize("isAnonymous()")
    String login(@NotBlank String username, @NotBlank String password);

    /**
     * Used to login a user by authentication token.
     * @param jwtToken The authentication token.
     */
    void login(@NotBlank String jwtToken);

}
