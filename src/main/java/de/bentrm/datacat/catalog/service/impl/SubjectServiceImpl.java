package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.XtdNest;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdSubject;
import de.bentrm.datacat.catalog.repository.NestRepository;
import de.bentrm.datacat.catalog.repository.PropertyRepository;
import de.bentrm.datacat.catalog.repository.SubjectRepository;
import de.bentrm.datacat.catalog.service.SubjectService;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Service
@Validated
@Transactional(readOnly = true)
public class SubjectServiceImpl extends AbstractServiceImpl<XtdSubject> implements SubjectService {

    private final NestRepository nestRepository;
    private final PropertyRepository propertyRepository;

    public SubjectServiceImpl(SessionFactory sessionFactory,
                              SubjectRepository repository,
                              NestRepository nestRepository,
                              PropertyRepository propertyRepository) {
        super(XtdSubject.class, sessionFactory, repository);
        this.nestRepository = nestRepository;
        this.propertyRepository = propertyRepository;
    }

    @Override
    public List<XtdNest> getGroupOfProperties(@NotNull XtdSubject subject) {
        Assert.notNull(subject.getId(), "Subject must be persistent.");
        final Iterable<String> nestIds = nestRepository.findGroupOfPropertiesIdBySubjectId(subject.getId());
        final Iterable<XtdNest> nests = nestRepository.findAllById(nestIds);
        final ArrayList<XtdNest> result = new ArrayList<>();
        nests.forEach(result::add);
        return result;
    }

    @Override
    public List<XtdProperty> getProperties(XtdSubject subject) {
        Assert.notNull(subject.getId(), "Subject must be persistent.");
        final Iterable<String> propertyIds = propertyRepository.findPropertyIdBySubjectId(subject.getId());
        final Iterable<XtdProperty> properties = propertyRepository.findAllById(propertyIds);
        final List<XtdProperty> result = new ArrayList<>();
        properties.forEach(result::add);
        return result;
    }
}
