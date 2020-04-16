package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.relationship.XtdRelGroups;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RelGroupsRepositoryExtension {

	Page<XtdRelGroups> findAllGroupedBy(String relatingThingId, Pageable pageable);

	Page<XtdRelGroups> findAllGrouping(String relatedThingId, Pageable pageable);

}
