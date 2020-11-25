package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.specification.QuerySpecification;
import de.bentrm.datacat.catalog.domain.XtdClassification;
import de.bentrm.datacat.catalog.repository.ClassificationRepository;
import de.bentrm.datacat.catalog.service.ClassificationService;
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
public class ClassificationServiceImpl implements ClassificationService {

    private final QueryDelegate<XtdClassification> queryDelegate;

    public ClassificationServiceImpl(ClassificationRepository repository) {
        this.queryDelegate = new QueryDelegate<>(repository);
    }

    @Override
    public @NotNull Optional<XtdClassification> findById(@NotNull String id) {
        return queryDelegate.findById(id);
    }

    @Override
    public @NotNull List<XtdClassification> findAllByIds(@NotNull List<String> ids) {
        return queryDelegate.findAllByIds(ids);
    }

    @Override
    public @NotNull Page<XtdClassification> findAll(@NotNull QuerySpecification specification) {
        return queryDelegate.findAll(specification);
    }

    @Override
    public @NotNull long count(@NotNull QuerySpecification specification) {
        return queryDelegate.count(specification);
    }
}
