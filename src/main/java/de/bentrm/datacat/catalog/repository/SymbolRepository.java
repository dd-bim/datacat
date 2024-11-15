package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdSymbol;

import java.util.List;

import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SymbolRepository extends EntityRepository<XtdSymbol> {

        @Query("""
            MATCH (n {id: $propertyId})-[:SYMBOLS]->(p:XtdSymbol)
            RETURN p.id""")
    List<String> findAllSymbolIdsAssignedToProperty(String propertyId);

}
