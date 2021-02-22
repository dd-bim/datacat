package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdRelationship;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.RelationshipRecordService;
import de.bentrm.datacat.catalog.service.value.RelationshipProperties;
import de.bentrm.datacat.catalog.service.value.ValueMapper;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
public abstract class AbstractRelationshipRecordServiceImpl<T extends XtdRelationship, R extends EntityRepository<T>>
        extends AbstractQueryServiceImpl<T, R>
        implements RelationshipRecordService<T> {

    protected final ValueMapper VALUE_MAPPER = ValueMapper.INSTANCE;
    private final CatalogCleanupService cleanupService;


    public AbstractRelationshipRecordServiceImpl(Class<T> domainClass,
                                                 SessionFactory sessionFactory,
                                                 R repository,
                                                 CatalogCleanupService cleanupService) {
        super(domainClass, sessionFactory, repository);
        this.cleanupService = cleanupService;
    }

    @Transactional
    @Override
    public T addRecord(@Valid RelationshipProperties properties,
                       @NotBlank String relatingRecordId,
                       @NotEmpty List<@NotBlank String> relatedRecordIds) {
        try {
            T newRecord = this.getDomainClass().getDeclaredConstructor().newInstance();

            log.trace("Mapping catalog record properties.");
            VALUE_MAPPER.setProperties(properties, newRecord);

            log.trace("Setting relating record with id: {}", relatingRecordId);
            this.setRelatingRecord(newRecord, relatingRecordId);

            log.trace("Setting related records with ids: {}", relatedRecordIds);
            this.setRelatedRecords(newRecord, relatedRecordIds);

            log.trace("Persisting new relationship record...");
            newRecord = this.getRepository().save(newRecord);
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
                .findById(id)
                .orElseThrow();

        log.trace("Purging related data of record {}...", id);
        cleanupService.purgeRelatedData(id);

        log.trace("Deleting record {}...", id);
        this.getRepository().delete(relationshipRecord);

        return relationshipRecord;
    }

    protected abstract void setRelatingRecord(@NotNull T relationshipRecord,
                                              @NotBlank String relatingRecordId);

    @Transactional
    @Override
    public @NotNull T setRelatedRecords(@NotBlank String relationshipId,
                                        @NotEmpty List<@NotBlank String> relatedRecordIds) {
        T record = this.getRepository()
                .findById(relationshipId)
                .orElseThrow();

        log.trace("Setting related records with ids: {}", relatedRecordIds);
        this.setRelatedRecords(record, relatedRecordIds);

        log.trace("Persisting updated relationship record...");
        record = this.getRepository().save(record);

        return record;
    }

    protected abstract void setRelatedRecords(@NotNull T relationshipRecord,
                                              @NotEmpty List<@NotBlank String> relatedRecordIds);
}
