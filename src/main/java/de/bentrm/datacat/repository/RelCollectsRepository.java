package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.relationship.XtdRelCollects;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelCollectsRepository extends Neo4jRepository<XtdRelCollects, String> {}
