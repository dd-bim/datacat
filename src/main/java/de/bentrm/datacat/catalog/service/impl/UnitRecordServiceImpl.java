package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdUnit;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdRational;
import de.bentrm.datacat.catalog.domain.XtdDimension;
import de.bentrm.datacat.catalog.domain.XtdMultiLanguageText;
import de.bentrm.datacat.catalog.repository.UnitRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.UnitRecordService;
import de.bentrm.datacat.catalog.service.dto.Relationships.CoefficientDtoProjection;
import de.bentrm.datacat.catalog.service.dto.Relationships.DimensionDtoProjection;
import de.bentrm.datacat.catalog.service.dto.Relationships.OffsetDtoProjection;
import de.bentrm.datacat.catalog.service.dto.Relationships.SymbolDtoProjection;
import de.bentrm.datacat.catalog.service.ConceptRecordService;
import de.bentrm.datacat.catalog.service.DimensionRecordService;
import de.bentrm.datacat.catalog.service.MultiLanguageTextRecordService;
import de.bentrm.datacat.catalog.service.PropertyRecordService;
import de.bentrm.datacat.catalog.service.RationalRecordService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jTemplate;

@Slf4j
@Service
@Validated
@Transactional(readOnly = true)
public class UnitRecordServiceImpl
        extends AbstractSimpleRecordServiceImpl<XtdUnit, UnitRepository>
        implements UnitRecordService {

        @Autowired
        private PropertyRecordService propertyRecordService;

        @Autowired
        private DimensionRecordService dimensionRecordService;

        @Autowired
        private MultiLanguageTextRecordService multiLanguageTextRecordService;

        @Autowired
        private RationalRecordService rationalRecordService;

        @Autowired
        private ConceptRecordService conceptRecordService;

    public UnitRecordServiceImpl(Neo4jTemplate neo4jTemplate,
            UnitRepository repository,
            CatalogCleanupService cleanupService) {
        super(XtdUnit.class, neo4jTemplate, repository, cleanupService);
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.Unit;
    }

    @Override
    public List<XtdProperty> getProperties(XtdUnit unit) {
        Assert.notNull(unit.getId(), "Unit must be persistent.");
        final List<String> propertyIds = getRepository().findAllPropertyIdsAssigningUnit(unit.getId());
        final Iterable<XtdProperty> properties = propertyRecordService.findAllEntitiesById(propertyIds);

        return StreamSupport
                .stream(properties.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public @NotNull XtdDimension getDimension(XtdUnit unit) {
        Assert.notNull(unit.getId(), "Unit must be persistent.");
        final String dimensionId = getRepository().findDimensionIdAssignedToUnit(unit.getId());
        if (dimensionId == null) {
            return null;
        }
        final XtdDimension dimension = dimensionRecordService.findByIdWithDirectRelations(dimensionId).orElseThrow();

        return dimension;
    }

    @Override
    public Optional<XtdMultiLanguageText> getSymbol(@NotNull XtdUnit unit) {
        Assert.notNull(unit.getId(), "Unit must be persistent.");
        final String symbolId = getRepository().findSymbolIdAssignedToUnit(unit.getId());
        if (symbolId == null) {
            return Optional.empty();
        }
        final Optional<XtdMultiLanguageText> symbol = multiLanguageTextRecordService.findByIdWithDirectRelations(symbolId);
        return symbol;
    }

    @Override
    public Optional<XtdRational> getCoefficient(@NotNull XtdUnit unit) {
        Assert.notNull(unit.getId(), "Unit must be persistent.");
        final String coefficientId = getRepository().findCoefficientIdAssignedToUnit(unit.getId());
        if (coefficientId == null) {
            return Optional.empty();
        }
        final Optional<XtdRational> coefficient = rationalRecordService.findByIdWithDirectRelations(coefficientId);
        return coefficient;
    }

    @Override
    public Optional<XtdRational> getOffset(@NotNull XtdUnit unit) {
        Assert.notNull(unit.getId(), "Unit must be persistent.");
        final String offsetId = getRepository().findOffsetIdAssignedToUnit(unit.getId());
        if (offsetId == null) {
            return Optional.empty();
        }
        final Optional<XtdRational> offset = rationalRecordService.findByIdWithDirectRelations(offsetId);
        return offset;
    }

    @Transactional
    @Override
    public @NotNull XtdUnit setRelatedRecords(@NotBlank String recordId,
            @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

        final XtdUnit unit = getRepository().findByIdWithDirectRelations(recordId).orElseThrow();

        switch (relationType) {
            case Symbol -> {
                if (unit.getSymbol() != null) {
                    throw new IllegalArgumentException("Unit already has a symbol.");
                } else if (relatedRecordIds.size() != 1) {
                    throw new IllegalArgumentException("Exactly one symbol must be assigned to a unit.");
                } else {
                    final XtdMultiLanguageText symbol = multiLanguageTextRecordService.findByIdWithDirectRelations(relatedRecordIds.get(0))
                            .orElseThrow();
                    unit.setSymbol(symbol);
                }
                neo4jTemplate.saveAs(unit, SymbolDtoProjection.class);
            }
            case Offset -> {
                if (unit.getOffset() != null) {
                    throw new IllegalArgumentException("Unit already has an offset.");
                } else if (relatedRecordIds.size() != 1) {
                    throw new IllegalArgumentException("Exactly one offset must be assigned to a unit.");
                } else {
                    final XtdRational offset = rationalRecordService.findByIdWithDirectRelations(relatedRecordIds.get(0)).orElseThrow();
                    unit.setOffset(offset);
                }
                neo4jTemplate.saveAs(unit, OffsetDtoProjection.class);
            }
            case Coefficient -> {
                if (unit.getCoefficient() != null) {
                    throw new IllegalArgumentException("Unit already has a coefficient.");
                } else if (relatedRecordIds.size() != 1) {
                    throw new IllegalArgumentException("Exactly one coefficient must be assigned to a unit.");
                } else {
                    final XtdRational coefficient = rationalRecordService.findByIdWithDirectRelations(relatedRecordIds.get(0)).orElseThrow();
                    unit.setCoefficient(coefficient);
                }
                neo4jTemplate.saveAs(unit, CoefficientDtoProjection.class);
            }
            case Dimension -> {
                if (unit.getDimension() != null) {
                    throw new IllegalArgumentException("Unit already has a dimension.");
                } else if (relatedRecordIds.size() != 1) {
                    throw new IllegalArgumentException("Exactly one dimension must be assigned to a unit.");
                } else {
                    final XtdDimension dimension = dimensionRecordService.findByIdWithDirectRelations(relatedRecordIds.get(0)).orElseThrow();
                    unit.setDimension(dimension);
                }
                neo4jTemplate.saveAs(unit, DimensionDtoProjection.class);
            }
            default -> conceptRecordService.setRelatedRecords(recordId, relatedRecordIds, relationType);
        }

        log.trace("Updated relationship: {}", unit);
        return unit;
    }
}
