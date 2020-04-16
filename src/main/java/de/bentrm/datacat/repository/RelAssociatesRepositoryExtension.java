package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.relationship.XtdRelAssociates;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RelAssociatesRepositoryExtension {

	Page<XtdRelAssociates> findAllAssociatedBy(String relatingThingId, Pageable pageable);

	Page<XtdRelAssociates> findAllAssociating(String relatedThingId, Pageable pageable);

}
