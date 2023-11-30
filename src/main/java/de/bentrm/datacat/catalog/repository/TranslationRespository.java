package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.Translation;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TranslationRespository extends EntityRepository<Translation> {

    @Query("MATCH (n:CatalogRecord)-->(t:Translation) WHERE n.id = $id RETURN t.id")
    List<String> findAllTranslationsByCatalogRecordId(String id);
}
