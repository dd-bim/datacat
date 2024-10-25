package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdText;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TextRepository extends EntityRepository<XtdText> {

    @Query("""
            MATCH (n {id: $multiLanguageTextId})-[:TEXTS]->(p:XtdText)
            RETURN p.id""")
    List<String> findAllTextIdsAssignedToMultiLanguageText(String multiLanguageTextId);
}
