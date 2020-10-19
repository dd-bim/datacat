package de.bentrm.datacat.base.repository;

import de.bentrm.datacat.auth.domain.EmailConfirmationRequest;

import java.util.Optional;

public interface EmailConfirmationRepository extends GraphEntityRepository<EmailConfirmationRequest> {

    Optional<EmailConfirmationRequest> findByToken(String token, int depth);

}
