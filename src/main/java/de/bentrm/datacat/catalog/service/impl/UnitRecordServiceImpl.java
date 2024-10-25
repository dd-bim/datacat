package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdUnit;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdRational;
import de.bentrm.datacat.catalog.domain.XtdSubject;
import de.bentrm.datacat.catalog.domain.XtdDimension;
import de.bentrm.datacat.catalog.domain.XtdMultiLanguageText;
import de.bentrm.datacat.catalog.repository.UnitRepository;
import de.bentrm.datacat.catalog.repository.DimensionRepository;
import de.bentrm.datacat.catalog.repository.MultiLanguageTextRepository;
import de.bentrm.datacat.catalog.repository.PropertyRepository;
import de.bentrm.datacat.catalog.repository.RationalRepository;
import de.bentrm.datacat.catalog.service.PropertyRecordService;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.UnitRecordService;
import de.bentrm.datacat.catalog.service.ConceptRecordService;
import lombok.extern.slf4j.Slf4j;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@Validated
@Transactional(readOnly = true)
public class UnitRecordServiceImpl
        extends AbstractSimpleRecordServiceImpl<XtdUnit, UnitRepository>
        implements UnitRecordService {

    private final PropertyRepository propertyRepository;
    private final DimensionRepository dimensionRepository;
    private final MultiLanguageTextRepository multiLanguageTextRepository;
    private final RationalRepository rationalRepository;
    private final ConceptRecordService conceptRecordService;

    public UnitRecordServiceImpl(SessionFactory sessionFactory,
            UnitRepository repository,
            PropertyRepository propertyRepository,
            DimensionRepository dimensionRepository,
            MultiLanguageTextRepository multiLanguageTextRepository,
            RationalRepository rationalRepository,
            ConceptRecordService conceptRecordService,
            CatalogCleanupService cleanupService) {
        super(XtdUnit.class, sessionFactory, repository, cleanupService);
        this.propertyRepository = propertyRepository;
        this.dimensionRepository = dimensionRepository;
        this.multiLanguageTextRepository = multiLanguageTextRepository;
        this.rationalRepository = rationalRepository;
        this.conceptRecordService = conceptRecordService;
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.Unit;
    }

    @Override
    public List<XtdProperty> getProperties(XtdUnit unit) {
        Assert.notNull(unit.getId(), "Unit must be persistent.");
        final List<String> propertyIds = propertyRepository.findAllPropertyIdsAssignedToUnit(unit.getId());
        final Iterable<XtdProperty> properties = propertyRepository.findAllById(propertyIds);

        return StreamSupport
                .stream(properties.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public XtdDimension getDimension(XtdUnit unit) {
        Assert.notNull(unit.getId(), "Unit must be persistent.");
        final String dimensionId = dimensionRepository.findDimensionIdAssignedToUnit(unit.getId());
        if (dimensionId == null) {
            return null;
        }
        final XtdDimension dimension = dimensionRepository.findById(dimensionId).orElse(null);

        return dimension;
    }

    @Transactional
    @Override
    public @NotNull XtdUnit setRelatedRecords(@NotBlank String recordId,
            @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

        final XtdUnit unit = getRepository().findById(recordId, 0).orElseThrow();

        switch (relationType) {
            case Symbol:
                if (unit.getSymbol() != null) {
                    throw new IllegalArgumentException("Unit already has a symbol.");
                } else if (relatedRecordIds.size() != 1) {
                    throw new IllegalArgumentException("Exactly one symbol must be assigned to a unit.");
                } else {
                    final XtdMultiLanguageText symbol = multiLanguageTextRepository.findById(relatedRecordIds.get(0))
                            .orElseThrow();
                    unit.setSymbol(symbol);
                }
                break;
            case Offset:
                if (unit.getOffset() != null) {
                    throw new IllegalArgumentException("Unit already has an offset.");
                } else if (relatedRecordIds.size() != 1) {
                    throw new IllegalArgumentException("Exactly one offset must be assigned to a unit.");
                } else {
                    final XtdRational offset = rationalRepository.findById(relatedRecordIds.get(0)).orElseThrow();
                    unit.setOffset(offset);
                }
                break;
            case Coefficient:
                if (unit.getCoefficient() != null) {
                    throw new IllegalArgumentException("Unit already has a coefficient.");
                } else if (relatedRecordIds.size() != 1) {
                    throw new IllegalArgumentException("Exactly one coefficient must be assigned to a unit.");
                } else {
                    final XtdRational coefficient = rationalRepository.findById(relatedRecordIds.get(0)).orElseThrow();
                    unit.setCoefficient(coefficient);
                }
                break;
            case Dimension:
                if (unit.getDimension() != null) {
                    throw new IllegalArgumentException("Unit already has a dimension.");
                } else if (relatedRecordIds.size() != 1) {
                    throw new IllegalArgumentException("Exactly one dimension must be assigned to a unit.");
                } else {
                    final XtdDimension dimension = dimensionRepository.findById(relatedRecordIds.get(0)).orElseThrow();
                    unit.setDimension(dimension);
                }
                break;
            default:
                conceptRecordService.setRelatedRecords(recordId, relatedRecordIds, relationType);
                break;
        }

        final XtdUnit persistentUnit = getRepository().save(unit);
        log.trace("Updated relationship: {}", persistentUnit);
        return persistentUnit;
    }
}
