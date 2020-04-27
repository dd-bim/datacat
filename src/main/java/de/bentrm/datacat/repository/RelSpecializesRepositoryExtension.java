package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.relationship.XtdRelSpecializes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RelSpecializesRepositoryExtension {

    Page<XtdRelSpecializes> findAllSpecializedBy(String relatingThingId, Pageable pageable);

    Page<XtdRelSpecializes> findAllSpecializing(String relatedThingId, Pageable pageable);
}
