package de.bentrm.datacat.auth.service;

import org.springframework.security.core.AuthenticationException;

public class EmailTakenException extends AuthenticationException {
    public EmailTakenException() {
        super("The provided Email is already taken.");
    }
}
