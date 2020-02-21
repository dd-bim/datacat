package de.bentrm.datacat.repository.relationship;

import de.bentrm.datacat.domain.relationship.XtdRelCollects;
import de.bentrm.datacat.repository.NamedEntityRepository;
import de.bentrm.datacat.repository.dto.RelCollectsQueryResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RelCollectsRepository extends NamedEntityRepository<XtdRelCollects> {

    @Query(
            value = "MATCH (c)-[:COLLECTS]->(r:xtdRelCollects)<-[:COLLECTED_IN]-(o) " +
                    "WITH r, c, count(o) AS itemCount " +
                    "MATCH (c)<-[:IS_NAME_OF]-(n) " +
                    "WITH r, c, n, itemCount " +
                    "ORDER BY n.name " +
                    "OPTIONAL MATCH (c)<-[:IS_DESCRIPTION_OF]-(d) " +
                    "RETURN r AS relationship, c AS collection, collect(n) AS names, collect(d) AS descriptions, itemCount",
            countQuery = "MATCH (c:xtdCollection)-[:COLLECTS]->(r:xtdRelCollects) RETURN count(r)"
    )
    Page<RelCollectsQueryResult> queryAll(Pageable pageable);

}
