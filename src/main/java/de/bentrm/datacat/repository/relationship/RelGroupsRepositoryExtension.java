package de.bentrm.datacat.repository.relationship;

import de.bentrm.datacat.domain.relationship.XtdRelGroups;
import de.bentrm.datacat.repository.RepositoryExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RelGroupsRepositoryExtension extends RepositoryExtension<XtdRelGroups> {

    Page<XtdRelGroups> findByRelatingObjectId(String relatingObjectId, Pageable pageable);
    Page<XtdRelGroups> findByRelatedObjectId(String relatedObjectId, Pageable pageable);

}
