package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdRelationship;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationshipRepository extends EntityRepository<XtdRelationship> {

}
