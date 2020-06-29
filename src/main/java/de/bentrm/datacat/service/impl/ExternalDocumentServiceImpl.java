package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.XtdExternalDocument;
import de.bentrm.datacat.graphql.dto.EntityInput;
import de.bentrm.datacat.graphql.dto.EntityUpdateInput;
import de.bentrm.datacat.repository.ExternalDocumentRepository;
import de.bentrm.datacat.service.ExternalDocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Transactional(readOnly = true)
@Validated
public class ExternalDocumentServiceImpl
        extends CatalogItemServiceImpl<XtdExternalDocument, EntityInput, EntityUpdateInput, ExternalDocumentRepository>
        implements ExternalDocumentService {

    Logger logger = LoggerFactory.getLogger(ExternalDocumentServiceImpl.class);

    public ExternalDocumentServiceImpl(ExternalDocumentRepository repository) {
        super(repository);
    }

    @Override
    protected XtdExternalDocument newEntityInstance() {
        return new XtdExternalDocument();
    }
}
