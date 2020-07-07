package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.EmailConfirmationRequest;

import java.util.Optional;

public interface EmailConfirmationRepository extends GraphEntityRepository<EmailConfirmationRequest> {

    Optional<EmailConfirmationRequest> findByToken(String token, int depth);

}
