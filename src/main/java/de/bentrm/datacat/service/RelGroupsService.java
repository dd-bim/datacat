package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.relationship.XtdRelGroups;
import de.bentrm.datacat.dto.RootInputDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RelGroupsService extends EntityService<XtdRelGroups>, NamedEntityService<XtdRelGroups> {

    XtdRelGroups create(String relatingObjectId, Set<String> relatedObjectsIds, RootInputDto dto);
    Optional<XtdRelGroups> delete(String id);

    XtdRelGroups addRelatedObjects(String id, List<String> relatedObjectsIds);
    XtdRelGroups removeRelatedObjects(String id, List<String> relatedObjectsIds);

    Page<XtdRelGroups> findByRelatingObjectId(String id, Pageable pageable);
    Page<XtdRelGroups> findByRelatedObjectId(String id, Pageable pageable);
}
