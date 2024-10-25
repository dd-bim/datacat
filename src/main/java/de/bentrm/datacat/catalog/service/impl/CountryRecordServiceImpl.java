package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdCountry;
import de.bentrm.datacat.catalog.repository.CountryRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.CountryRecordService;
import de.bentrm.datacat.catalog.service.ConceptRecordService;
import lombok.extern.slf4j.Slf4j;
import de.bentrm.datacat.catalog.domain.XtdSubdivision;
import de.bentrm.datacat.catalog.domain.XtdSubject;
import de.bentrm.datacat.catalog.repository.SubdivisionRepository;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.util.Assert;

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
public class CountryRecordServiceImpl
        extends AbstractSimpleRecordServiceImpl<XtdCountry, CountryRepository>
        implements CountryRecordService {

            private final SubdivisionRepository subdivisionRepository;
            private final ConceptRecordService conceptRecordService;

    public CountryRecordServiceImpl(SessionFactory sessionFactory,
                                     CountryRepository repository,
                                    SubdivisionRepository subdivisionRepository,
                                    ConceptRecordService conceptRecordService,
                                     CatalogCleanupService cleanupService) {
        super(XtdCountry.class, sessionFactory, repository, cleanupService);
        this.subdivisionRepository = subdivisionRepository;
        this.conceptRecordService = conceptRecordService;
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.Country;
    }

    @Override
    public List<XtdSubdivision> getSubdivisions(XtdCountry country) {
        Assert.notNull(country.getId(), "Subdivision must be persistent.");
        final List<String> subdivisionIds = subdivisionRepository.findAllSubdivisionIdsAssignedToCountry(country.getId());
        final Iterable<XtdSubdivision> subdivisions = subdivisionRepository.findAllById(subdivisionIds);

        return StreamSupport
                .stream(subdivisions.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public @NotNull XtdCountry setRelatedRecords(@NotBlank String recordId,
                                                    @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

        final XtdCountry country = getRepository().findById(recordId, 0).orElseThrow();

        switch (relationType) {
            case Subdivisions:
                final Iterable<XtdSubdivision> items = subdivisionRepository.findAllById(relatedRecordIds);
                final List<XtdSubdivision> related = StreamSupport
                        .stream(items.spliterator(), false)
                        .collect(Collectors.toList());

                country.getSubdivisions().clear();
                country.getSubdivisions().addAll(related);
                break;
        
            default:
                conceptRecordService.setRelatedRecords(recordId, relatedRecordIds, relationType);
                break;
        }

        final XtdCountry persistentCountry = getRepository().save(country);
        log.trace("Updated relationship: {}", persistentCountry);
        return persistentCountry;
    }
}
