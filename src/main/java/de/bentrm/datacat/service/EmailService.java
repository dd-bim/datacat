package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.EmailConfirmationRequest;

public interface EmailService {

    void sendEmailConfirmation(EmailConfirmationRequest emailConfirmationRequest);
}
