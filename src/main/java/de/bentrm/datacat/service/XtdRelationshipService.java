package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.relationship.XtdRelDocuments;
import de.bentrm.datacat.domain.relationship.XtdRelGroups;
import de.bentrm.datacat.domain.relationship.XtdRelationship;
import de.bentrm.datacat.graphql.dto.XtdRelGroupsInput;
import org.springframework.data.domain.Page;

public interface XtdRelationshipService extends EntityService<XtdRelationship> {

    XtdRelDocuments findRelDocumentsById(String id);
    Page<XtdRelDocuments> findAllRelDocuments(int pageNumber, int pageSize);
    Page<XtdRelDocuments> findRelDocumentsByRelatingDocument(String relatingDocumentId, int pageNumber, int pageSize);

    XtdRelGroups createRelGroups(XtdRelGroupsInput dto);
    XtdRelGroups findRelGroupsById(String id);
    Page<XtdRelGroups> findRelGroupsByRelatingObjectId(String relatingObjectId, int pageNumber, int pageSize);
    Page<XtdRelGroups> findAllRelGroups(int pageNumber, int pageSize);

}
