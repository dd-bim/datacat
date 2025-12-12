package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdValue;

import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ValueRepository extends EntityRepository<XtdValue> {

    @Query("""
            MATCH (v:XtdValue {id: $id})
            RETURN v""")
    Optional<XtdValue> findByIdWithoutRelations(String id);

    @Query("""
            MATCH (n:XtdValue {id: $valueId})<-[:ORDERED_VALUE]-(p:XtdOrderedValue)
            RETURN p.id""")
    List<String> findOrderedValueIdByValueId(String valueId);
}
