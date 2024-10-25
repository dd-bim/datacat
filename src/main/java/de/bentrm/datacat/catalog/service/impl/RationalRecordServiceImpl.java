package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdRational;
import de.bentrm.datacat.catalog.domain.XtdSubject;
import de.bentrm.datacat.catalog.repository.RationalRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.RationalRecordService;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Service
@Validated
@Transactional(readOnly = true)
public class RationalRecordServiceImpl
        extends AbstractSimpleRecordServiceImpl<XtdRational, RationalRepository>
        implements RationalRecordService {

    public RationalRecordServiceImpl(SessionFactory sessionFactory,
                                     RationalRepository repository,
                                     CatalogCleanupService cleanupService) {
        super(XtdRational.class, sessionFactory, repository, cleanupService);
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.Rational;
    }

    @Transactional
    @Override
    public @NotNull XtdRational setRelatedRecords(@NotBlank String recordId,
                                                    @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

        final XtdRational rational = getRepository().findById(recordId, 0).orElseThrow();

        return rational;
    }
}
