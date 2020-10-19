package de.bentrm.datacat.auth.service;

import de.bentrm.datacat.auth.domain.EmailConfirmationRequest;

public interface EmailService {

    void sendEmailConfirmation(EmailConfirmationRequest emailConfirmationRequest);
}
