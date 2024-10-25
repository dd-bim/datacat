package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends EntityRepository<XtdProperty> {

    @Query("""
            MATCH (n {id: $subjectId})-[:PROPERTIES]->(p:XtdProperty)
            RETURN p.id""")
    List<String> findAllPropertyIdsAssignedToSubject(String subjectId);

    @Query("""
            MATCH (n {id: $valueListId})<-[:POSSIBLE_VALUES]->(p:XtdProperty)
            RETURN p.id""")
    List<String> findAllPropertyIdsAssignedToValueList(String valueListId);

    @Query("""
            MATCH (n {id: $unitId})<-[:UNITS]->(p:XtdProperty)
            RETURN p.id""")
    List<String> findAllPropertyIdsAssignedToUnit(String unitId);
}
