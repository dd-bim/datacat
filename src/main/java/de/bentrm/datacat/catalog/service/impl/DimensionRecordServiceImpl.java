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
import de.bentrm.datacat.catalog.service.dto.Relationships.DimensionExponentsDtoProjection;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

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

        final XtdDimension dimension = getRepository().findById(recordId).orElseThrow();

        switch (relationType) {
            case ThermodynamicTemperatureExponent -> {
                if (dimension.getThermodynamicTemperatureExponent() != null) {
                    throw new IllegalArgumentException("ThermodynamicTemperatureExponent already set.");
                } else if (relatedRecordIds.size() != 1) {
                    throw new IllegalArgumentException(
                            "ThermodynamicTemperatureExponent requires exactly one related record.");
                } else {
                    final XtdRational rational = rationalRecordService.findByIdWithDirectRelations(relatedRecordIds.get(0)).orElseThrow();
                    dimension.setThermodynamicTemperatureExponent(rational);
                }
            }
            case AmountOfSubstanceExponent -> {
                if (dimension.getAmountOfSubstanceExponent() != null) {
                    throw new IllegalArgumentException("AmountOfSubstanceExponent already set.");
                } else if (relatedRecordIds.size() != 1) {
                    throw new IllegalArgumentException(
                            "AmountOfSubstanceExponent requires exactly one related record.");
                } else {
                    final XtdRational rational = rationalRecordService.findByIdWithDirectRelations(relatedRecordIds.get(0)).orElseThrow();
                    dimension.setAmountOfSubstanceExponent(rational);
                }
            }
            case LengthExponent -> {
                if (dimension.getLengthExponent() != null) {
                    throw new IllegalArgumentException("LengthExponent already set.");
                } else if (relatedRecordIds.size() != 1) {
                    throw new IllegalArgumentException("LengthExponent requires exactly one related record.");
                } else {
                    final XtdRational rational = rationalRecordService.findByIdWithDirectRelations(relatedRecordIds.get(0)).orElseThrow();
                    dimension.setLengthExponent(rational);
                }
            }
            case MassExponent -> {
                if (dimension.getMassExponent() != null) {
                    throw new IllegalArgumentException("MassExponent already set.");
                } else if (relatedRecordIds.size() != 1) {
                    throw new IllegalArgumentException("MassExponent requires exactly one related record.");
                } else {
                    final XtdRational rational = rationalRecordService.findByIdWithDirectRelations(relatedRecordIds.get(0)).orElseThrow();
                    dimension.setMassExponent(rational);
                }
            }
            case TimeExponent -> {
                if (dimension.getTimeExponent() != null) {
                    throw new IllegalArgumentException("TimeExponent already set.");
                } else if (relatedRecordIds.size() != 1) {
                    throw new IllegalArgumentException("TimeExponent requires exactly one related record.");
                } else {
                    final XtdRational rational = rationalRecordService.findByIdWithDirectRelations(relatedRecordIds.get(0)).orElseThrow();
                    dimension.setTimeExponent(rational);
                }
            }
            case ElectricCurrentExponent -> {
                if (dimension.getElectricCurrentExponent() != null) {
                    throw new IllegalArgumentException("ElectricCurrentExponent already set.");
                } else if (relatedRecordIds.size() != 1) {
                    throw new IllegalArgumentException("ElectricCurrentExponent requires exactly one related record.");
                } else {
                    final XtdRational rational = rationalRecordService.findByIdWithDirectRelations(relatedRecordIds.get(0)).orElseThrow();
                    dimension.setElectricCurrentExponent(rational);
                }
            }
            case LuminousIntensityExponent -> {
                if (dimension.getLuminousIntensityExponent() != null) {
                    throw new IllegalArgumentException("LuminousIntensityExponent already set.");
                } else if (relatedRecordIds.size() != 1) {
                    throw new IllegalArgumentException(
                            "LuminousIntensityExponent requires exactly one related record.");
                } else {
                    final XtdRational rational = rationalRecordService.findByIdWithDirectRelations(relatedRecordIds.get(0)).orElseThrow();
                    dimension.setLuminousIntensityExponent(rational);
                }
            }
            default -> conceptRecordService.setRelatedRecords(recordId, relatedRecordIds, relationType);
        }

        neo4jTemplate.saveAs(dimension, DimensionExponentsDtoProjection.class);
        log.trace("Updated dimension: {}", dimension);
        return dimension;
    }
}
