package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.XtdBag;
import de.bentrm.datacat.catalog.repository.BagRepository;
import de.bentrm.datacat.catalog.service.BagRecordService;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.EntityMapper;
import de.bentrm.datacat.catalog.service.value.CatalogRecordProperties;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Slf4j
@Service
@Validated
@Transactional(readOnly = true)
public class BagRecordServiceImpl
        extends AbstractSimpleRecordServiceImpl<XtdBag, BagRepository>
        implements BagRecordService {

    public BagRecordServiceImpl(SessionFactory sessionFactory,
                                BagRepository repository,
                                CatalogCleanupService cleanupService) {
        super(XtdBag.class, sessionFactory, repository, cleanupService);
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.Bag;
    }

    @Transactional
    @Override
    public @NotNull XtdBag addRecord(CatalogRecordProperties properties) {
        final XtdBag newEntity = new XtdBag();
        EntityMapper.INSTANCE.setProperties(properties, newEntity);
        final XtdBag persistentEntity = getRepository().save(newEntity);
        log.trace("Persisted new catalog entry: {}", persistentEntity);
        return persistentEntity;
    }
}
