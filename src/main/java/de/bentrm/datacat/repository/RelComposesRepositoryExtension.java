package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.relationship.XtdRelComposes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RelComposesRepositoryExtension {

    Page<XtdRelComposes> findAllComposedBy(String relatingThingId, Pageable pageable);

    Page<XtdRelComposes> findAllComposing(String relatedThingId, Pageable pageable);
}
