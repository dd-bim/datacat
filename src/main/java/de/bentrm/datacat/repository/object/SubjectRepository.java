package de.bentrm.datacat.repository.object;

import de.bentrm.datacat.domain.XtdSubject;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends Neo4jRepository<XtdSubject, String>, SubjectRepositoryExtension {}
