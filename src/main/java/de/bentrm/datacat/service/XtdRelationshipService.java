package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.relationship.XtdRelGroups;
import de.bentrm.datacat.domain.relationship.XtdRelationship;
import de.bentrm.datacat.dto.XtdRelGroupsInputDto;

import java.util.List;

public interface XtdRelationshipService extends NamedEntityService<XtdRelationship> {

    XtdRelGroups createRelGroups(XtdRelGroupsInputDto dto);
    Iterable<XtdRelGroups> findRelGroups();
    XtdRelGroups findRelGroupsByUniqueId(String uniqueId);
    List<XtdRelGroups> findAsscociationsByRelatingObjectUniqueId(String uniqueId);

}
