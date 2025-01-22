package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdCountry;
import de.bentrm.datacat.catalog.repository.CountryRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.CountryRecordService;
import de.bentrm.datacat.catalog.service.SubdivisionRecordService;
import de.bentrm.datacat.catalog.service.dto.Relationships.SubdivisionsDtoProjection;
import de.bentrm.datacat.catalog.service.ConceptRecordService;
import lombok.extern.slf4j.Slf4j;
import de.bentrm.datacat.catalog.domain.XtdSubdivision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.util.Assert;

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
public class CountryRecordServiceImpl
        extends AbstractSimpleRecordServiceImpl<XtdCountry, CountryRepository>
        implements CountryRecordService {

            @Autowired
            private SubdivisionRecordService subdivisionRecordService;

            @Autowired
            private ConceptRecordService conceptRecordService;

    public CountryRecordServiceImpl(Neo4jTemplate neo4jTemplate,
                                     CountryRepository repository,
                                     CatalogCleanupService cleanupService) {
        super(XtdCountry.class, neo4jTemplate, repository, cleanupService);
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.Country;
    }

    @Override
    public List<XtdSubdivision> getSubdivisions(XtdCountry country) {
        Assert.notNull(country.getId(), "Subdivision must be persistent.");
        final List<String> subdivisionIds = getRepository().findAllSubdivisionIdsAssignedToCountry(country.getId());
        final Iterable<XtdSubdivision> subdivisions = subdivisionRecordService.findAllEntitiesById(subdivisionIds);

        return StreamSupport
                .stream(subdivisions.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public @NotNull XtdCountry setRelatedRecords(@NotBlank String recordId,
                                                    @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

        final XtdCountry country = getRepository().findById(recordId).orElseThrow();

        switch (relationType) {
            case Subdivisions -> {
                final Iterable<XtdSubdivision> items = subdivisionRecordService.findAllEntitiesById(relatedRecordIds);
                final List<XtdSubdivision> related = StreamSupport
                        .stream(items.spliterator(), false)
                        .collect(Collectors.toList());

                country.getSubdivisions().clear();
                country.getSubdivisions().addAll(related);
                    }
        
            default -> conceptRecordService.setRelatedRecords(recordId, relatedRecordIds, relationType);
        }

        neo4jTemplate.saveAs(country, SubdivisionsDtoProjection.class); 
        log.trace("Updated relationship: {}", country);
        return country;
    }
}
