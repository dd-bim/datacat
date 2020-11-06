package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.domain.Entity;
import de.bentrm.datacat.base.repository.GraphEntityRepository;
import de.bentrm.datacat.base.specification.QuerySpecification;
import de.bentrm.datacat.catalog.service.QueryService;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QueryDelegate<T extends Entity> implements QueryService<T> {

    private final GraphEntityRepository<T> repository;

    QueryDelegate(GraphEntityRepository<T> repository) {
        this.repository = repository;
    }

    @Override
    public @NotNull Optional<T> findById(@NotNull String id) {
        return repository.findById(id);
    }

    @Override
    public @NotNull Page<T> findAll(@NotNull QuerySpecification specification) {
        return repository.findAll(specification);
    }

    @Override
    public @NotNull long count(@NotNull QuerySpecification specification) {
        return repository.count(specification);
    }

    @Override
    public @NotNull List<T> findAllByIds(@NotNull List<String> ids) {
        Iterable<T> source = repository.findAllById(ids);
        List<T> target = new ArrayList<>();
        source.forEach(target::add);
        return target;
    }

}
