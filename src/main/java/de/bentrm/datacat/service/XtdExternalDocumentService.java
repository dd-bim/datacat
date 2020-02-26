package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.XtdExternalDocument;
import de.bentrm.datacat.domain.XtdSubject;
import de.bentrm.datacat.dto.XtdExternalDocumentInputDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface XtdExternalDocumentService extends NamedEntityService<XtdExternalDocument> {

    XtdExternalDocument createExternalDocument(XtdExternalDocumentInputDto dto);
    XtdExternalDocument deleteExternalDocument(String uniqueId);
    Page<XtdExternalDocument> findAllExternalDocuments(int pageNumber, int pageSize);
    Page<XtdExternalDocument> findExternalDocumentsByTerm(String term, int pageNumber, int pageSize);

}
