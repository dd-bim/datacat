package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.XtdLanguage;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends UniqueEntityRepository<XtdLanguage> {

    XtdLanguage findByLanguageCode(String languageCode);

    @Query("MATCH (l:XtdLanguage)<-[:IN_LANGUAGE]-(n:XtdLanguageRepresentation) WHERE n.uniqueId = {uniqueId} RETURN l")
    XtdLanguage findByLanguageRepresentationUniqueId(@Param("uniqueId") String uniqueId);

}
