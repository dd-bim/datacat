package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdCountry;

import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends EntityRepository<XtdCountry> {

    @Query("""
            MATCH (n:XtdCountry {id: $countryId})-[:SUBDIVISIONS]->(p:XtdSubdivision)
            RETURN p.id""")
    List<String> findAllSubdivisionIdsAssignedToCountry(String countryId);

    @Query("MATCH (n:XtdCountry) WHERE n.code = $countryCode RETURN n")
    Optional<XtdCountry> findByCode(String countryCode);
}
