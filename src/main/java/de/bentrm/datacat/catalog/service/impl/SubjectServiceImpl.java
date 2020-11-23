package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.specification.QuerySpecification;
import de.bentrm.datacat.catalog.domain.XtdNest;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdSubject;
import de.bentrm.datacat.catalog.repository.NestRepository;
import de.bentrm.datacat.catalog.repository.PropertyRepository;
import de.bentrm.datacat.catalog.repository.SubjectRepository;
import de.bentrm.datacat.catalog.service.EntityMapper;
import de.bentrm.datacat.catalog.service.SubjectService;
import de.bentrm.datacat.catalog.service.value.EntryValue;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Validated
@Transactional(readOnly = true)
public class SubjectServiceImpl implements SubjectService {

    private final EntityMapper entityMapper = EntityMapper.INSTANCE;
    private final SubjectRepository repository;
    private final NestRepository nestRepository;
    private final PropertyRepository propertyRepository;
    private final QueryDelegate<XtdSubject> queryDelegate;

    public SubjectServiceImpl(SubjectRepository repository,
                              NestRepository nestRepository,
                              PropertyRepository propertyRepository) {
        this.repository = repository;
        this.nestRepository = nestRepository;
        this.propertyRepository = propertyRepository;
        this.queryDelegate = new QueryDelegate<>(repository);
    }

    @Transactional
    @Override
    public @NotNull XtdSubject create(EntryValue value) {
        final XtdSubject item = new XtdSubject();
        entityMapper.setProperties(value, item);
        return repository.save(item);
    }

    @Override
    public @NotNull Optional<XtdSubject> findById(@NotNull String id) {
        return queryDelegate.findById(id);
    }

    @Override
    public @NotNull List<XtdSubject> findAllByIds(@NotNull List<String> ids) {
        return queryDelegate.findAllByIds(ids);
    }

    @Override
    public @NotNull Page<XtdSubject> findAll(@NotNull QuerySpecification specification) {
        return queryDelegate.findAll(specification);
    }

    @Override
    public @NotNull long count(@NotNull QuerySpecification specification) {
        return queryDelegate.count(specification);
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
