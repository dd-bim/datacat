package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.specification.QuerySpecification;
import de.bentrm.datacat.catalog.domain.XtdSubject;
import de.bentrm.datacat.catalog.repository.SubjectRepository;
import de.bentrm.datacat.catalog.service.EntityMapper;
import de.bentrm.datacat.catalog.service.NewSubjectService;
import de.bentrm.datacat.catalog.service.value.EntryValue;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
@Validated
@Transactional(readOnly = true)
public class NewSubjectServiceImpl implements NewSubjectService {

    private final EntityMapper entityMapper = EntityMapper.INSTANCE;
    private final SubjectRepository repository;
    private final QueryServiceDelegate<XtdSubject> queryDelegate;

    public NewSubjectServiceImpl(SubjectRepository repository) {
        this.repository = repository;
        this.queryDelegate = new QueryServiceDelegate<>(repository);
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
}
