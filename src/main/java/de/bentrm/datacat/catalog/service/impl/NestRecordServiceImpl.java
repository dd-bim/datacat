package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.XtdNest;
import de.bentrm.datacat.catalog.repository.NestRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.NestRecordService;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Service
@Validated
@Transactional(readOnly = true)
public class NestRecordServiceImpl extends AbstractSimpleRecordServiceImpl<XtdNest> implements NestRecordService {

    public NestRecordServiceImpl(SessionFactory sessionFactory,
                                 NestRepository repository,
                                 CatalogCleanupService cleanupService) {
        super(XtdNest.class, sessionFactory, repository, cleanupService);
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.Nest;
    }
}
