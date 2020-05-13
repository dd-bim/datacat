package de.bentrm.datacat.service;

import de.bentrm.datacat.auth.UserSession;
import de.bentrm.datacat.graphql.dto.SignupInput;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public interface UserService {

    @EventListener
    void onApplicationEvent(ContextRefreshedEvent event);

    @PreAuthorize("isAnonymous()")
    UserSession signup(@NotNull @Valid SignupInput signupInput);

    @PreAuthorize("isAnonymous()")
    UserSession login(@NotBlank String username, @NotBlank String password);

}
