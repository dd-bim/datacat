package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.Measure;
import de.bentrm.datacat.catalog.repository.MeasureRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.MeasureRecordService;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Service
@Validated
@Transactional(readOnly = true)
public class MeasureRecordServiceImpl
        extends AbstractSimpleRecordServiceImpl<Measure, MeasureRepository>
        implements MeasureRecordService {

    public MeasureRecordServiceImpl(SessionFactory sessionFactory,
                                    MeasureRepository repository,
                                    CatalogCleanupService cleanupService) {
        super(Measure.class, sessionFactory, repository, cleanupService);
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.Measure;
    }
}
