package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdMultiLanguageText;

import java.util.List;

import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MultiLanguageTextRepository extends EntityRepository<XtdMultiLanguageText> {
        
    @Query("""
        MATCH (n:XtdMultiLanguageText {id: $multiLanguageTextId})-[:TEXTS]->(p:XtdText)
        RETURN p.id""")
List<String> findAllTextIdsAssignedToMultiLanguageText(String multiLanguageTextId);
}
