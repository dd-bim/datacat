package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.relationship.XtdRelActsUpon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RelActsUponRepositoryExtension {

    Page<XtdRelActsUpon> findAllActedUponBy(String relatingThingId, Pageable pageable);

    Page<XtdRelActsUpon> findAllActingUpon(String relatedThingId, Pageable pageable);
}
