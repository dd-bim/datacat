package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.repository.RootRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Slf4j
@Service
@Transactional
public class CatalogCleanupServiceImpl implements CatalogCleanupService {

    private final RootRepository rootRepository;

    public CatalogCleanupServiceImpl(RootRepository rootRepository) {

        this.rootRepository = rootRepository;

    }

    @Override
    public void deleteNodeWithRelationships(@NotBlank String recordId) {
        Assert.hasText(recordId, "the given record id may not be blank");
        rootRepository.deleteNodeAndRelationships(recordId);
    }

    @Override
    public void purgeRelationship(@NotBlank String recordId, @NotBlank String relatedRecordId, @NotNull SimpleRelationType relationType) {
        Assert.hasText(recordId, "the given record id may not be blank");
        Assert.hasText(relatedRecordId, "the given related record id may not be blank");
        Assert.notNull(relationType, "the given relation type may not be null");
        log.info("Purging relationship: from={}, to={}, type={}", recordId, relatedRecordId, relationType.getRelationProperty());
        rootRepository
                .removeRelationship(recordId, relatedRecordId, relationType.getRelationProperty());
        log.info("Relationship purged successfully");
    }
}
