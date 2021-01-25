package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.repository.RelationshipRepository;
import de.bentrm.datacat.catalog.repository.TranslationRespository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.constraints.NotBlank;

@Service
@Transactional
public class CatalogCleanupServiceImpl implements CatalogCleanupService {

    private final RelationshipRepository relationshipRepository;
    private final TranslationRespository translationRespository;

    public CatalogCleanupServiceImpl(RelationshipRepository relationshipRepository,
                                     TranslationRespository translationRespository) {

        this.relationshipRepository = relationshipRepository;
        this.translationRespository = translationRespository;
    }

    @Override
    public void purgeRelatedData(@NotBlank String recordId) {
        Assert.hasText(recordId, "the given record id may not be blank");
        this.purgeTranslations(recordId);
        this.purgeRelationships(recordId);
    }

    @Override
    public void purgeTranslations(@NotBlank String recordId) {
        Assert.hasText(recordId, "the given record id may not be blank");
        translationRespository
                .findAllTranslationsByCatalogRecordId(recordId)
                .forEach(translationRespository::deleteById);
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
}
