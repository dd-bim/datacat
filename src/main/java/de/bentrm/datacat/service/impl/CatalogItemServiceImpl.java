package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.CatalogItem;
import de.bentrm.datacat.graphql.dto.EntityInput;
import de.bentrm.datacat.graphql.dto.EntityUpdateInput;
import de.bentrm.datacat.graphql.dto.InputMapper;
import de.bentrm.datacat.graphql.dto.TextInput;
import de.bentrm.datacat.repository.GraphEntityRepository;
import de.bentrm.datacat.service.CrudEntityService;
import de.bentrm.datacat.service.RelGroupsService;
import de.bentrm.datacat.specification.QuerySpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static de.bentrm.datacat.service.impl.ServiceUtil.sanitizeTextInputValue;

@Slf4j
@Validated
@Transactional(readOnly = true)
public abstract class CatalogItemServiceImpl<
        T extends CatalogItem,
        C extends EntityInput,
        U extends EntityUpdateInput,
        R extends GraphEntityRepository<T>>
        implements CrudEntityService<T, C, U> {

    protected final R repository;

    @Autowired
    @Lazy
    protected RelGroupsService relGroupsService;

    @Autowired
    protected InputMapper inputMapper;

    public CatalogItemServiceImpl(R repository) {
        this.repository = repository;
    }

    @Transactional
    @Override
    public T create(@Valid C dto) {
        T newEntity = newEntityInstance();
        setEntityProperties(newEntity, dto);
        return repository.save(newEntity);
    }

    protected void setEntityProperties(T entity, C dto) {
        entity.setId(dto.getId());

        List<TextInput> names = dto.getNames();
        for (TextInput name : names) {
            entity.setName(name.getId(), name.getLanguageCode(), sanitizeTextInputValue(name.getValue()));
        }
    }

    @Transactional
    @Override
    public T update(@Valid U dto) {
        T entity = repository
                .findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("No Object with id " + dto.getId() + " found."));

        log.trace("Updating entity {}", entity);
        updateEntityProperties(entity, dto);
        log.trace("New state {}", entity);

        return repository.save(entity);
    }

    protected void updateEntityProperties(T entity, U dto) {
        List<TextInput> names = dto.getNames();
        for (TextInput name : names) {
            entity.setName(name.getId(), name.getLanguageCode(), sanitizeTextInputValue(name.getValue()));
        }
    }

    // TODO: Delete all relationships that this entity is on the relating side of
    @Transactional
    @Override
    public Optional<T> delete(@NotNull String id) {
        Optional<T> result = repository.findById(id);
        result.ifPresent(repository::delete);
        return result;
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

    protected abstract T newEntityInstance();
}
