package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.XtdExternalDocument;
import de.bentrm.datacat.dto.XtdExternalDocumentInputDto;

public interface XtdExternalDocumentService extends NamedEntityService<XtdExternalDocument> {

    XtdExternalDocument create(XtdExternalDocumentInputDto dto);
}
