package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdDimension;
import de.bentrm.datacat.catalog.domain.XtdUnit;
import de.bentrm.datacat.catalog.domain.XtdQuantityKind;
import de.bentrm.datacat.catalog.repository.QuantityKindRepository;
import de.bentrm.datacat.catalog.repository.UnitRepository;
import de.bentrm.datacat.catalog.repository.DimensionRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.ConceptRecordService;
import de.bentrm.datacat.catalog.service.QuantityKindRecordService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@Validated
@Transactional(readOnly = true)
public class QuantityKindRecordServiceImpl
        extends AbstractSimpleRecordServiceImpl<XtdQuantityKind, QuantityKindRepository>
        implements QuantityKindRecordService {

            private final UnitRepository unitRepository;
            private final DimensionRepository dimensionRepository;
            private final ConceptRecordService conceptRecordService;

    public QuantityKindRecordServiceImpl(Neo4jTemplate neo4jTemplate,
                                     QuantityKindRepository repository,
                                     UnitRepository unitRepository,
                                     DimensionRepository dimensionRepository,
                                     ConceptRecordService conceptRecordService,
                                     CatalogCleanupService cleanupService) {
        super(XtdQuantityKind.class, neo4jTemplate, repository, cleanupService);
        this.unitRepository = unitRepository;
        this.dimensionRepository = dimensionRepository;
        this.conceptRecordService = conceptRecordService;
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.QuantityKind;
    }

    @Override
    public List<XtdUnit> getUnits(XtdQuantityKind quantityKind) {
        Assert.notNull(quantityKind, "QuantityKind must be persistent." );
        final List<String> unitIds = unitRepository.findAllUnitIdsAssignedToQuantityKind(quantityKind.getId());
        final Iterable<XtdUnit> units = unitRepository.findAllById(unitIds);

        return StreamSupport
                .stream(units.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public XtdDimension getDimension(XtdQuantityKind quantityKind) {
        Assert.notNull(quantityKind, "QuantityKind must be persistent." );
        final String dimensionId = dimensionRepository.findDimensionIdAssignedToQuantityKind(quantityKind.getId());
        if (dimensionId == null) {
            return null;
        }
        final XtdDimension dimension = dimensionRepository.findById(dimensionId).orElse(null);
        return dimension;
    }

    @Transactional
    @Override
    public @NotNull XtdQuantityKind setRelatedRecords(@NotBlank String recordId,
                                                    @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

        final XtdQuantityKind quantityKind = getRepository().findById(recordId).orElseThrow();
        
        switch (relationType) {
            case Units -> {
                final Iterable<XtdUnit> units = unitRepository.findAllById(relatedRecordIds);
                final List<XtdUnit> relatedUnits = StreamSupport
                        .stream(units.spliterator(), false)
                        .collect(Collectors.toList());
                
                quantityKind.getUnits().clear();
                quantityKind.getUnits().addAll(relatedUnits);
                    }
            case Dimension -> {
                if (quantityKind.getDimension() != null) {
                    throw new IllegalArgumentException("QuantityKind already has a dimension assigned.");
                } else if (relatedRecordIds.size() != 1) {
                    throw new IllegalArgumentException("Exactly one dimension must be assigned.");
                } else {
                    final XtdDimension dimension = dimensionRepository.findById(relatedRecordIds.get(0)).orElseThrow();
                    quantityKind.setDimension(dimension);
                }   }
            default -> conceptRecordService.setRelatedRecords(recordId, relatedRecordIds, relationType);
        }

        final XtdQuantityKind persistentQuantityKind = getRepository().save(quantityKind);
        log.trace("Updated relationship: {}", persistentQuantityKind);
        return persistentQuantityKind;
    }
}
