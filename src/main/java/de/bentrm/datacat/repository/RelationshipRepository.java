package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.relationship.XtdRelationship;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationshipRepository extends GraphEntityRepository<XtdRelationship> {

}
