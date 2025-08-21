package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdDimension;
import de.bentrm.datacat.catalog.domain.XtdRational;
import de.bentrm.datacat.catalog.repository.DimensionRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.ConceptRecordService;
import de.bentrm.datacat.catalog.service.DimensionRecordService;
import de.bentrm.datacat.catalog.service.RationalRecordService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Slf4j
@Service
@Validated
@Transactional(readOnly = true)
public class DimensionRecordServiceImpl
        extends AbstractSimpleRecordServiceImpl<XtdDimension, DimensionRepository>
        implements DimensionRecordService {

    @Autowired
    private RationalRecordService rationalRecordService;

    @Autowired
    private ConceptRecordService conceptRecordService;

    public DimensionRecordServiceImpl(Neo4jTemplate neo4jTemplate,
            DimensionRepository repository,
            CatalogCleanupService cleanupService) {
        super(XtdDimension.class, neo4jTemplate, repository, cleanupService);
    }

    @Override
    public @NotNull Page<XtdDimension> findAll(@NotNull de.bentrm.datacat.base.specification.QuerySpecification specification) {
        // Use optimized query when no complex filters are applied
        if (isSimpleQuery(specification)) {
            List<XtdDimension> dimensions = findDimensionsWithRelations(specification);
            Pageable pageable = specification.getPageable().orElse(PageRequest.of(0, 20));
            return PageableExecutionUtils.getPage(dimensions, pageable, 
                () -> getRepository().count());
        }
        // Fallback to default implementation for complex queries
        return super.findAll(specification);
    }

    private boolean isSimpleQuery(de.bentrm.datacat.base.specification.QuerySpecification specification) {
        // Consider it simple if there are no filters, only pagination and sorting
        return specification.getFilters() == null || specification.getFilters().isEmpty();
    }

    private List<XtdDimension> findDimensionsWithRelations(de.bentrm.datacat.base.specification.QuerySpecification specification) {
        Pageable pageable = specification.getPageable().orElse(PageRequest.of(0, 20));
        String query = buildOptimizedDimensionQuery(pageable);
        return getNeo4jTemplate().findAll(query, XtdDimension.class);
    }

    private String buildOptimizedDimensionQuery(Pageable pageable) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("MATCH (d:XtdDimension) ");
        
        // Add optional matches for commonly used relations
        queryBuilder.append("OPTIONAL MATCH (d)<-[:DIMENSIONS]-(properties:XtdProperty) ");
        queryBuilder.append("OPTIONAL MATCH (d)<-[:DIMENSIONS]-(units:XtdUnit) ");
        queryBuilder.append("OPTIONAL MATCH (d)-[:THERMODYNAMIC_TEMPERATURE_EXPONENT]->(thermExponent:XtdRational) ");
        queryBuilder.append("OPTIONAL MATCH (d)-[:ELECTRIC_CURRENT_EXPONENT]->(electricExponent:XtdRational) ");
        queryBuilder.append("OPTIONAL MATCH (d)-[:LENGTH_EXPONENT]->(lengthExponent:XtdRational) ");
        queryBuilder.append("OPTIONAL MATCH (d)-[:MASS_EXPONENT]->(massExponent:XtdRational) ");
        queryBuilder.append("OPTIONAL MATCH (d)-[:TIME_EXPONENT]->(timeExponent:XtdRational) ");
        
        queryBuilder.append("RETURN d, ");
        queryBuilder.append("collect(DISTINCT properties) as properties, ");
        queryBuilder.append("collect(DISTINCT units) as units, ");
        queryBuilder.append("collect(DISTINCT thermExponent) as thermExponent, ");
        queryBuilder.append("collect(DISTINCT electricExponent) as electricExponent, ");
        queryBuilder.append("collect(DISTINCT lengthExponent) as lengthExponent, ");
        queryBuilder.append("collect(DISTINCT massExponent) as massExponent, ");
        queryBuilder.append("collect(DISTINCT timeExponent) as timeExponent ");
        
        // Add sorting if specified
        if (pageable.getSort().isSorted()) {
            queryBuilder.append("ORDER BY ");
            String sortClause = pageable.getSort().stream()
                    .map(order -> "d." + order.getProperty() + " " + order.getDirection())
                    .collect(Collectors.joining(", "));
            queryBuilder.append(sortClause).append(" ");
        }
        
        // Add pagination
        queryBuilder.append("SKIP ").append(pageable.getOffset()).append(" ");
        queryBuilder.append("LIMIT ").append(pageable.getPageSize());
        
        return queryBuilder.toString();
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.Dimension;
    }

    @Override
    public Optional<XtdRational> getThermodynamicTemperatureExponent(XtdDimension dimension) {
       final String id = getRepository().findThermodynamicTemperatureExponentIdAssignedToDimension(dimension.getId());
       if (id == null) {
           return Optional.empty();
       }
       final Optional<XtdRational> rational = rationalRecordService.findByIdWithDirectRelations(id);
         return rational;
    }

    @Override
    public Optional<XtdRational> getAmountOfSubstanceExponent(XtdDimension dimension) {
        final String id = getRepository().findAmountOfSubstanceExponentIdAssignedToDimension(dimension.getId());
        if (id == null) {
            return Optional.empty();
        }
        final Optional<XtdRational> rational = rationalRecordService.findByIdWithDirectRelations(id);
        return rational;
    }

    @Override
    public Optional<XtdRational> getLengthExponent(XtdDimension dimension) {
        final String id = getRepository().findLengthExponentIdAssignedToDimension(dimension.getId());
        if (id == null) {
            return Optional.empty();
        }
        final Optional<XtdRational> rational = rationalRecordService.findByIdWithDirectRelations(id);
        return rational;
    }

    @Override
    public Optional<XtdRational> getMassExponent(XtdDimension dimension) {
        final String id = getRepository().findMassExponentIdAssignedToDimension(dimension.getId());
        if (id == null) {
            return Optional.empty();
        }
        final Optional<XtdRational> rational = rationalRecordService.findByIdWithDirectRelations(id);
        return rational;
    }

    @Override
    public Optional<XtdRational> getTimeExponent(XtdDimension dimension) {
        final String id = getRepository().findTimeExponentIdAssignedToDimension(dimension.getId());
        if (id == null) {
            return Optional.empty();
        }
        final Optional<XtdRational> rational = rationalRecordService.findByIdWithDirectRelations(id);
        return rational;
    }

    @Override
    public Optional<XtdRational> getElectricCurrentExponent(XtdDimension dimension) {
        final String id = getRepository().findElectricCurrentExponentIdAssignedToDimension(dimension.getId());
        if (id == null) {
            return Optional.empty();
        }
        final Optional<XtdRational> rational = rationalRecordService.findByIdWithDirectRelations(id);
        return rational;
    }

    @Override
    public Optional<XtdRational> getLuminousIntensityExponent(XtdDimension dimension) {
        final String id = getRepository().findLuminousIntensityExponentIdAssignedToDimension(dimension.getId());
        if (id == null) {
            return Optional.empty();
        }
        final Optional<XtdRational> rational = rationalRecordService.findByIdWithDirectRelations(id);
        return rational;
    }

    @Transactional
    @Override
    public @NotNull XtdDimension setRelatedRecords(@NotBlank String recordId,
            @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

        final XtdDimension dimension = getRepository().findById(recordId).orElseThrow(() -> new IllegalArgumentException("No record with id " + recordId + " found."));
        conceptRecordService.setRelatedRecords(recordId, relatedRecordIds, relationType);
        return dimension;
    }
}
