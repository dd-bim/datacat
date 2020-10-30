package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.specification.QuerySpecification;
import de.bentrm.datacat.catalog.domain.XtdBag;
import de.bentrm.datacat.catalog.repository.BagRepository;
import de.bentrm.datacat.catalog.service.BagService;
import de.bentrm.datacat.catalog.service.EntityMapper;
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
public class BagServiceImpl implements BagService {

    private final EntityMapper entityMapper = EntityMapper.INSTANCE;
    private final BagRepository repository;
    private final QueryServiceDelegate<XtdBag> queryDelegate;

    public BagServiceImpl(BagRepository repository) {
        this.repository = repository;
        this.queryDelegate = new QueryServiceDelegate<>(repository);
    }

    @Transactional
    @Override
    public @NotNull XtdBag create(EntryValue value) {
        final XtdBag item = new XtdBag();
        entityMapper.setProperties(value, item);
        return repository.save(item);
    }

    @Override
    public @NotNull Optional<XtdBag> findById(@NotNull String id) {
        return queryDelegate.findById(id);
    }

    @Override
    public @NotNull List<XtdBag> findAllByIds(@NotNull List<String> ids) {
        return queryDelegate.findAllByIds(ids);
    }

    @Override
    public @NotNull Page<XtdBag> findAll(@NotNull QuerySpecification specification) {
        return queryDelegate.findAll(specification);
    }

    @Override
    public @NotNull long count(@NotNull QuerySpecification specification) {
        return queryDelegate.count(specification);
    }
}
