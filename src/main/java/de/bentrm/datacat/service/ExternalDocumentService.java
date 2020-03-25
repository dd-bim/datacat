package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.XtdExternalDocument;
import de.bentrm.datacat.graphql.dto.ExternalDocumentInput;

import java.util.Optional;

public interface ExternalDocumentService extends EntityService<XtdExternalDocument> {

    XtdExternalDocument create(ExternalDocumentInput dto);
    Optional<XtdExternalDocument> delete(String id);
}
