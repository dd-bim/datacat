package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.XtdUnit;
import de.bentrm.datacat.catalog.repository.UnitRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.UnitRecordService;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Service
@Validated
@Transactional(readOnly = true)
public class UnitRecordServiceImpl extends AbstractSimpleRecordServiceImpl<XtdUnit> implements UnitRecordService {

    public UnitRecordServiceImpl(SessionFactory sessionFactory,
                                 UnitRepository repository,
                                 CatalogCleanupService cleanupService) {
        super(XtdUnit.class, sessionFactory, repository, cleanupService);
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.Unit;
    }
}
