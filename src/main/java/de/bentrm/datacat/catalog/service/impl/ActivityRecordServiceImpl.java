package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.XtdActivity;
import de.bentrm.datacat.catalog.service.ActivityRecordService;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Service
@Validated
@Transactional(readOnly = true)
public class ActivityRecordServiceImpl extends AbstractSimpleRecordServiceImpl<XtdActivity> implements ActivityRecordService {

    public ActivityRecordServiceImpl(SessionFactory sessionFactory,
                                     EntityRepository<XtdActivity> repository,
                                     CatalogCleanupService cleanupService) {
        super(XtdActivity.class, sessionFactory, repository, cleanupService);
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.Activity;
    }
}
