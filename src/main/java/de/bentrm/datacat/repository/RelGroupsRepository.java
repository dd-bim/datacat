package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.relationship.XtdRelGroups;
import org.springframework.stereotype.Repository;

@Repository
public interface RelGroupsRepository extends RelationshipRepository<XtdRelGroups, String>, RelGroupsRepositoryExtension {

}