package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.repository.RelationshipRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Service
@Transactional
public class CatalogCleanupServiceImpl implements CatalogCleanupService {

    private final RelationshipRepository relationshipRepository;

    public CatalogCleanupServiceImpl(RelationshipRepository relationshipRepository) {

        this.relationshipRepository = relationshipRepository;

    }

    @Override
    public void purgeRelatedData(@NotBlank String recordId) {
        Assert.hasText(recordId, "the given record id may not be blank");
        this.purgeRelationships(recordId);
    }

    @Override
    public void purgeRelationships(@NotBlank String recordId) {
        Assert.hasText(recordId, "the given record id may not be blank");
        relationshipRepository
                .findAllRelationshipsByRelatingId(recordId)
                .forEach(relationshipRepository::deleteById);
        relationshipRepository
                .findAllSingularRelationshipsByRelatedId(recordId)
                .forEach(relationshipRepository::deleteById);
    }

    @Override
    public void purgeRelationship(@NotBlank String recordId, @NotBlank String relatedRecordId, @NotNull SimpleRelationType relationType) {
        Assert.hasText(recordId, "the given record id may not be blank");
        Assert.hasText(relatedRecordId, "the given related record id may not be blank");
        Assert.notNull(relationType, "the given relation type may not be null");
        relationshipRepository
                .removeRelationship(recordId, relatedRecordId, relationType.getRelationProperty());
    }
}
