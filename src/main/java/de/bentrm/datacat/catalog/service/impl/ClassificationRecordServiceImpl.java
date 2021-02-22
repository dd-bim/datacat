package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.XtdClassification;
import de.bentrm.datacat.catalog.repository.ClassificationRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.ClassificationRecordService;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Service
@Validated
@Transactional(readOnly = true)
public class ClassificationRecordServiceImpl
        extends AbstractSimpleRecordServiceImpl<XtdClassification, ClassificationRepository>
        implements ClassificationRecordService {

    public ClassificationRecordServiceImpl(SessionFactory sessionFactory,
                                           ClassificationRepository repository,
                                           CatalogCleanupService cleanupService) {
        super(XtdClassification.class, sessionFactory, repository, cleanupService);
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.Classification;
    }
}
