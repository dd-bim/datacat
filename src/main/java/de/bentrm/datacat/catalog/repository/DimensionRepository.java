package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdDimension;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DimensionRepository extends EntityRepository<XtdDimension> {

        @Query("""
                        MATCH (n {id: $propertyId})-[:DIMENSION]->(p:XtdDimension)
                        RETURN p.id""")
        String findDimensionIdAssignedToProperty(String propertyId);

        @Query("""
                        MATCH (n:XtdDimension {id: $dimension})-[:THERMODYNAMIC_TEMPERATURE_EXPONENT]->(p:XtdRational)
                        RETURN p.id""")
        String findThermodynamicTemperatureExponentIdAssignedToDimension(String dimension);

        @Query("""
                        MATCH (n:XtdDimension {id: $dimension})-[:ELECTRIC_CURRENT_EXPONENT]->(p:XtdRational)
                        RETURN p.id""")
        String findElectricCurrentExponentIdAssignedToDimension(String dimension);

        @Query("""
                        MATCH (n:XtdDimension {id: $dimension})-[:LENGTH_EXPONENT]->(p:XtdRational)
                        RETURN p.id""")
        String findLengthExponentIdAssignedToDimension(String dimension);

        @Query("""
                        MATCH (n:XtdDimension {id: $dimension})-[:LUMINOUS_INTENSITY_EXPONENT]->(p:XtdRational)
                        RETURN p.id""")
        String findLuminousIntensityExponentIdAssignedToDimension(String dimension);

        @Query("""
                        MATCH (n:XtdDimension {id: $dimension})-[:AMOUNT_OF_SUBSTANCE_EXPONENT]->(p:XtdRational)
                        RETURN p.id""")
        String findAmountOfSubstanceExponentIdAssignedToDimension(String dimension);

        @Query("""
                        MATCH (n:XtdDimension {id: $dimension})-[:MASS_EXPONENT]->(p:XtdRational)
                        RETURN p.id""")
        String findMassExponentIdAssignedToDimension(String dimension);

        @Query("""
                        MATCH (n:XtdDimension {id: $dimension})-[:TIME_EXPONENT]->(p:XtdRational)
                        RETURN p.id""")
        String findTimeExponentIdAssignedToDimension(String dimension);
}
