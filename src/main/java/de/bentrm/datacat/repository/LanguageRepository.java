package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.XtdLanguage;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends EntityRepository<XtdLanguage> {

    XtdLanguage findByLanguageCode(String languageCode);

    @Query("MATCH (l:XtdLanguage)<-[:IN_LANGUAGE]-(n:XtdLanguageRepresentation) WHERE n.id = {id} RETURN l")
    XtdLanguage findByLanguageRepresentationId(@Param("id") String id);

}
