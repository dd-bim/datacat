package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.XtdNest;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdSubject;
import de.bentrm.datacat.catalog.repository.NestRepository;
import de.bentrm.datacat.catalog.repository.PropertyRepository;
import de.bentrm.datacat.catalog.repository.SubjectRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.SubjectRecordService;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@Validated
@Transactional(readOnly = true)
public class SubjectRecordServiceImpl
        extends AbstractSimpleRecordServiceImpl<XtdSubject, SubjectRepository>
        implements SubjectRecordService {

    private final NestRepository nestRepository;
    private final PropertyRepository propertyRepository;

    public SubjectRecordServiceImpl(SessionFactory sessionFactory,
                                    SubjectRepository repository,
                                    CatalogCleanupService cleanupService,
                                    NestRepository nestRepository,
                                    PropertyRepository propertyRepository) {
        super(XtdSubject.class, sessionFactory, repository, cleanupService);
        this.nestRepository = nestRepository;
        this.propertyRepository = propertyRepository;
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.Subject;
    }

    @Override
    public List<XtdNest> getGroupOfProperties(@NotNull XtdSubject subject) {
        Assert.notNull(subject.getId(), "Subject must be persistent.");
        final Iterable<String> nestIds = nestRepository.findAllGroupOfPropertiesIdsAssignedToSubject(subject.getId());
        final Iterable<XtdNest> nests = nestRepository.findAllById(nestIds);

        return StreamSupport
                .stream(nests.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public List<XtdProperty> getProperties(XtdSubject subject) {
        Assert.notNull(subject.getId(), "Subject must be persistent.");
        final List<String> propertyIds = propertyRepository.findAllPropertyIdsAssignedToSubject(subject.getId());
        final Iterable<XtdProperty> properties = propertyRepository.findAllById(propertyIds);

        return StreamSupport
                .stream(properties.spliterator(), false)
                .collect(Collectors.toList());
    }
}
