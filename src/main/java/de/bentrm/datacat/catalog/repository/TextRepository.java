package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdText;

import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TextRepository extends EntityRepository<XtdText> {

    @Query("""
            MATCH (x:XtdText {id: $textId})-[:LANGUAGE]->(y:XtdLanguage)
            RETURN y.id""")
    String findLanguageIdByTextId(String textId);

}