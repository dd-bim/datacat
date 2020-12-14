package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.CatalogItem;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.SimpleRecordService;
import de.bentrm.datacat.catalog.service.value.CatalogEntryProperties;
import de.bentrm.datacat.catalog.service.value.ValueMapper;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.session.SessionFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Slf4j
public abstract class AbstractSimpleRecordServiceImpl<T extends CatalogItem>
        extends AbstractQueryServiceImpl<T>
        implements SimpleRecordService<T> {

    protected final ValueMapper VALUE_MAPPER = ValueMapper.INSTANCE;
    private final CatalogCleanupService cleanupService;

    public AbstractSimpleRecordServiceImpl(Class<T> domainClass,
                                           SessionFactory sessionFactory,
                                           EntityRepository<T> repository,
                                           CatalogCleanupService cleanupService) {
        super(domainClass, sessionFactory, repository);
        this.cleanupService = cleanupService;
    }

    @Override
    public @NotNull CatalogItem addRecord(@Valid CatalogEntryProperties properties) {
        try {
            T newRecord = this.getDomainClass().getDeclaredConstructor().newInstance();
            VALUE_MAPPER.setProperties(properties, newRecord);
            newRecord = this.getRepository().save(newRecord);
            log.trace("Persisted new catalog entry: {}", newRecord);
            return newRecord;
        } catch (ReflectiveOperationException e) {
            log.warn("Can not instantiate catalog records of type: {}", this.getDomainClass());
            throw new IllegalArgumentException("unsupported record type");
        }

    }

    @Override
    public @NotNull T removeRecord(@NotBlank String id) {
        log.trace("Deleting simple catalog record with id {}...", id);
        final T entry = this.getRepository()
                .findById(id)
                .orElseThrow();

        cleanupService.purgeRelatedData(id);
        this.getRepository().deleteById(id);

        log.trace("Catalog item deleted: {}", entry);

        return entry;
    }
}