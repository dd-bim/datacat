package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.XtdExternalDocument;
import de.bentrm.datacat.dto.XtdExternalDocumentInputDto;
import org.springframework.data.domain.Page;

public interface XtdExternalDocumentService extends EntityService<XtdExternalDocument>,
        NamedEntityService<XtdExternalDocument> {

    XtdExternalDocument createExternalDocument(XtdExternalDocumentInputDto dto);
    XtdExternalDocument deleteExternalDocument(String id);
    Page<XtdExternalDocument> findAllExternalDocuments(int pageNumber, int pageSize);
    Page<XtdExternalDocument> findExternalDocumentsByTerm(String term, int pageNumber, int pageSize);

}
