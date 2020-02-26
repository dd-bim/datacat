package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.relationship.XtdRelDocuments;
import de.bentrm.datacat.domain.relationship.XtdRelGroups;
import de.bentrm.datacat.domain.relationship.XtdRelationship;
import de.bentrm.datacat.dto.XtdRelGroupsInputDto;
import org.springframework.data.domain.Page;

public interface XtdRelationshipService extends NamedEntityService<XtdRelationship> {

    XtdRelDocuments findRelDocumentsByUniqueId(String uniqueId);
    Page<XtdRelDocuments> findAllRelDocuments(int pageNumber, int pageSize);
    Page<XtdRelDocuments> findRelDocumentsByRelatingDocument(String relatingDocumentUniqueId, int pageNumber, int pageSize);

    XtdRelGroups createRelGroups(XtdRelGroupsInputDto dto);
    XtdRelGroups findRelGroupsByUniqueId(String uniqueId);
    Page<XtdRelGroups> findRelGroupsByRelatingObjectUniqueId(String relatingObjectUniqueId, int pageNumber, int pageSize);
    Page<XtdRelGroups> findAllRelGroups(int pageNumber, int pageSize);

}
