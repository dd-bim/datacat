package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.specification.QuerySpecification;
import de.bentrm.datacat.catalog.domain.XtdUnit;
import de.bentrm.datacat.catalog.repository.UnitRepository;
import de.bentrm.datacat.catalog.service.EntityMapper;
import de.bentrm.datacat.catalog.service.UnitService;
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
public class UnitServiceImpl implements UnitService {

    private final EntityMapper entityMapper = EntityMapper.INSTANCE;
    private final UnitRepository repository;
    private final QueryServiceDelegate<XtdUnit> queryDelegate;

    public UnitServiceImpl(UnitRepository repository) {
        this.repository = repository;
        this.queryDelegate = new QueryServiceDelegate<>(repository);
    }

    @Transactional
    @Override
    public @NotNull XtdUnit create(EntryValue value) {
        final XtdUnit item = new XtdUnit();
        entityMapper.setProperties(value, item);
        return repository.save(item);
    }

    @Override
    public @NotNull Optional<XtdUnit> findById(@NotNull String id) {
        return queryDelegate.findById(id);
    }

    @Override
    public @NotNull List<XtdUnit> findAllByIds(@NotNull List<String> ids) {
        return queryDelegate.findAllByIds(ids);
    }

    @Override
    public @NotNull Page<XtdUnit> findAll(@NotNull QuerySpecification specification) {
        return queryDelegate.findAll(specification);
    }

    @Override
    public @NotNull long count(@NotNull QuerySpecification specification) {
        return queryDelegate.count(specification);
    }
}
