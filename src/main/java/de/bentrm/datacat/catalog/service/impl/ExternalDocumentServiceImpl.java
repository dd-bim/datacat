package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdExternalDocument;
import de.bentrm.datacat.catalog.service.ExternalDocumentService;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional(readOnly = true)
public class ExternalDocumentServiceImpl extends AbstractServiceImpl<XtdExternalDocument> implements ExternalDocumentService {

    public ExternalDocumentServiceImpl(SessionFactory sessionFactory, EntityRepository<XtdExternalDocument> repository) {
        super(XtdExternalDocument.class, sessionFactory, repository);
    }
}
