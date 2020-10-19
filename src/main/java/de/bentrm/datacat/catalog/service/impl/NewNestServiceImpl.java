package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.specification.QuerySpecification;
import de.bentrm.datacat.catalog.domain.XtdNest;
import de.bentrm.datacat.catalog.repository.NestRepository;
import de.bentrm.datacat.catalog.service.EntityMapper;
import de.bentrm.datacat.catalog.service.NewNestService;
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
public class NewNestServiceImpl implements NewNestService {

    private final EntityMapper entityMapper = EntityMapper.INSTANCE;
    private final NestRepository repository;
    private final QueryServiceDelegate<XtdNest> queryDelegate;

    public NewNestServiceImpl(NestRepository repository) {
        this.repository = repository;
        this.queryDelegate = new QueryServiceDelegate<>(repository);
    }

    @Transactional
    @Override
    public @NotNull XtdNest create(EntryValue value) {
        final XtdNest item = new XtdNest();
        entityMapper.setProperties(value, item);
        return repository.save(item);
    }

    @Override
    public @NotNull Optional<XtdNest> findById(@NotNull String id) {
        return queryDelegate.findById(id);
    }

    @Override
    public @NotNull List<XtdNest> findAllByIds(@NotNull List<String> ids) {
        return queryDelegate.findAllByIds(ids);
    }

    @Override
    public @NotNull Page<XtdNest> findAll(@NotNull QuerySpecification specification) {
        return queryDelegate.findAll(specification);
    }

    @Override
    public @NotNull long count(@NotNull QuerySpecification specification) {
        return queryDelegate.count(specification);
    }
}
