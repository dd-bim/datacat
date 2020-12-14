package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.XtdExternalDocument;
import de.bentrm.datacat.catalog.repository.ExternalDocumentRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.ExternalDocumentRecordService;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Service
@Validated
@Transactional(readOnly = true)
public class ExternalDocumentRecordServiceImpl
        extends AbstractSimpleRecordServiceImpl<XtdExternalDocument>
        implements ExternalDocumentRecordService {

    public ExternalDocumentRecordServiceImpl(SessionFactory sessionFactory,
                                             ExternalDocumentRepository repository,
                                             CatalogCleanupService cleanupService) {
        super(XtdExternalDocument.class, sessionFactory, repository, cleanupService);
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.ExternalDocument;
    }
}
