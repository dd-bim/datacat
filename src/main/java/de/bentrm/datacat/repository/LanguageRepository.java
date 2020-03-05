package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.XtdLanguage;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LanguageRepository extends EntityRepository<XtdLanguage> {

    @Query("MATCH (l:XtdLanguage)<-[:IN_LANGUAGE]-(n:XtdLanguageRepresentation) WHERE n.id = {id} RETURN l")
    Optional<XtdLanguage> findByLanguageRepresentationId(@Param("id") String id);

}
