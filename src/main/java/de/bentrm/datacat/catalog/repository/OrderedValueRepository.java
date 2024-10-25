package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdOrderedValue;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderedValueRepository extends EntityRepository<XtdOrderedValue> {

    @Query("""
            MATCH (n {id: $valueListId})-[:VALUES]->(p:XtdOrderedValue)
            RETURN p.id""")
    List<String> findAllOrderedValueIdsAssignedToValueList(String valueListId);

//     @Query("""
//             MATCH (n {id: $valueId})<-[:ORDERED_VALUE]-(p:XtdOrderedValue)
//             RETURN p""")
//     List<String> findAllOrderedValueIdsAssignedToValue(String valueId);
}
