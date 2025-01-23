package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.AbstractRelationship;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.RelationshipRecordService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

import org.springframework.data.neo4j.core.Neo4jTemplate;

@Slf4j
public abstract class AbstractRelationshipRecordServiceImpl<T extends AbstractRelationship, R extends EntityRepository<T>>
        extends AbstractQueryServiceImpl<T, R>
        implements RelationshipRecordService<T> {

    private final CatalogCleanupService cleanupService;

    public AbstractRelationshipRecordServiceImpl(Class<T> domainClass,
            Neo4jTemplate neo4jTemplate,
            R repository,
            CatalogCleanupService cleanupService) {
        super(domainClass, neo4jTemplate, repository);
        this.cleanupService = cleanupService;
    }

    @Transactional
    @Override
    public T addRecord(@NotBlank String id,
            @NotBlank String relatingRecordId,
            @NotEmpty List<@NotBlank String> relatedRecordIds) {
        try {
            T newRecord = this.getDomainClass().getDeclaredConstructor().newInstance();

            newRecord.setId(id);
            log.trace("Persisting new relationship record...");
            newRecord = this.getRepository().save(newRecord);

            log.trace("Setting relating record with id: {}", relatingRecordId);
            newRecord = this.setRelatingRecord(newRecord, relatingRecordId);
            log.info("Set relating record with id: {}", relatingRecordId);

            log.trace("Setting related records with ids: {}", relatedRecordIds);
            newRecord = this.setRelatedRecords(newRecord, relatedRecordIds);
            log.info("Set related records with ids: {}", relatedRecordIds);

            log.trace("Persisted new relationship record with id: {}", newRecord.getId());

            return newRecord;
        } catch (ReflectiveOperationException e) {
            log.warn("Can not instantiate catalog records of type: {}", this.getDomainClass());
            throw new IllegalArgumentException("unsupported record type");
        }
    }

    @Transactional
    @Override
    public @NotNull T removeRecord(@NotBlank String id) {
        final T relationshipRecord = this.getRepository()
                .findByIdWithDirectRelations(id)
                .orElseThrow(() -> new IllegalArgumentException("No record with id " + id + " found."));

        log.trace("Deleting record {}...", id);
        cleanupService.deleteNodeWithRelationships(id);

        return relationshipRecord;
    }

    @Transactional
    @Override
    public @NotNull T removeRelationship(@NotBlank String recordId, @NotBlank String relatedRecordId, @NotNull SimpleRelationType relationType) {
        log.trace("Deleting relationship from record with id {}...", recordId);
        final T entry = this.getRepository()
                .findByIdWithDirectRelations(recordId)
                .orElseThrow(() -> new IllegalArgumentException("No record with id " + recordId + " found."));

        cleanupService.purgeRelationship(recordId, relatedRecordId, relationType);
        return entry;
    }

    protected abstract @NotNull T setRelatingRecord(@NotNull T relationshipRecord,
            @NotBlank String relatingRecordId);

    @Transactional
    @Override
    public @NotNull T setRelatedRecords(@NotBlank String relationshipId,
            @NotEmpty List<@NotBlank String> relatedRecordIds) {
        T record = this.getRepository()
                .findById(relationshipId)
                .orElseThrow(() -> new IllegalArgumentException("No record with id " + relationshipId + " found."));

        log.trace("Setting related records with ids: {}", relatedRecordIds);
        this.setRelatedRecords(record, relatedRecordIds);

        log.trace("Persisting updated relationship record...");
        record = this.getRepository().save(record);

        return record;
    }

    protected abstract @NotNull T setRelatedRecords(@NotNull T relationshipRecord,
            @NotEmpty List<@NotBlank String> relatedRecordIds);

    @Transactional
    @Override
    public @NotNull T setRelatedRecords(@NotBlank String recordId,
            @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {
        T record = this.getRepository()
                .findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("No record with id " + recordId + " found."));
        return record;
    }
}
