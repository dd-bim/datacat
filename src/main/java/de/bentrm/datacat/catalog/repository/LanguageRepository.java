package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdLanguage;

import java.util.Optional;

import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends EntityRepository<XtdLanguage> {

        @Query("MATCH (n:XtdLanguage) WHERE n.code = $languageCode RETURN n")
        Optional<XtdLanguage> findByCode(String languageCode);
}
