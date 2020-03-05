package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.XtdExternalDocument;
import de.bentrm.datacat.dto.ExternalDocumentInputDto;

import java.util.Optional;

public interface ExternalDocumentService extends EntityService<XtdExternalDocument>,
        NamedEntityService<XtdExternalDocument> {

    XtdExternalDocument create(ExternalDocumentInputDto dto);
    Optional<XtdExternalDocument> delete(String id);
}
