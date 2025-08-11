package de.bentrm.datacat.catalog.service;

import de.bentrm.datacat.auth.domain.User;

import jakarta.validation.constraints.NotNull;

public interface UserService extends QueryService<User> {

    User findByUsername(@NotNull String id);

    void updateName(@NotNull String username, @NotNull String firstname, @NotNull String lastname);

    void updateEmail(@NotNull String username, @NotNull String newEmail);
}
