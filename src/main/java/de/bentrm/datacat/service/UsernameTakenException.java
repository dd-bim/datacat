package de.bentrm.datacat.service;

import org.springframework.security.core.AuthenticationException;

public class UsernameTakenException extends AuthenticationException {

    public UsernameTakenException() {
        super("The provided username is already taken.");
    }
}
