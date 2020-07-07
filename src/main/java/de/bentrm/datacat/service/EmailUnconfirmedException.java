package de.bentrm.datacat.service;

import org.springframework.security.authentication.AccountStatusException;

public class EmailUnconfirmedException extends AccountStatusException {
    public EmailUnconfirmedException() {
        super("The provided email of this account has not been confirmed yet.");
    }
}
