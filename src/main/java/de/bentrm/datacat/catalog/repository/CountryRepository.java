package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdCountry;

import java.util.List;

import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends EntityRepository<XtdCountry> {

        @Query("""
            MATCH (n {id: $countryId})-[:SUBDIVISIONS]->(p:XtdSubdivision)
            RETURN p.id""")
    List<String> findAllSubdivisionIdsAssignedToCountry(String countryId);

    @Query("""
            MATCH (n {id: $conceptId})-[:COUNTRY_OF_ORIGIN]->(p:XtdSubdivision)
            RETURN p.id""")
    String findCountryIdAssignedToConcept(String conceptId);
}
