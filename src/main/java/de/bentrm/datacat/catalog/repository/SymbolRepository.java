package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdSymbol;

import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SymbolRepository extends EntityRepository<XtdSymbol> {

    @Query("""
        MATCH (n:XtdSymbol {id: $symbolId})-[:SUBJECT]->(p:XtdSubject)
        RETURN p.id""")
    String findSubjectIdAssignedToSymbol(String symbolId);

    // @Query("""
    //     MATCH (n:XtdSymbol {id: $symbolId})-[:SUBJECT]->(p:XtdSubject)
    //     RETURN p""")
    // Optional<XtdSubject> findSubjectAssignedToSymbol(String symbolId);

    @Query("""
        MATCH (n:XtdSymbol {id: $symbolId})-[:SYMBOL]->(p:XtdText)
        RETURN p.id""")
    String findSymbolText(String symbolId);
}
