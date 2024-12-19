package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdOrderedValue;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderedValueRepository extends EntityRepository<XtdOrderedValue> {

    @Query("""
            MATCH (n:XtdOrderedValue {id: $OrderedValueId})<-[:VALUES]-(p:XtdValueList)
            RETURN p.id""")
    List<String> findAllValueListIdsAssigningOrderedValue(String OrderedValueId);

    @Query("""
            MATCH (n:XtdOrderedValue {id: $orderedValueId})-[:ORDERED_VALUE]->(p:XtdValue)
            RETURN p.id""")
    String findValueIdAssignedToOrderedValue(String orderedValueId);

}
