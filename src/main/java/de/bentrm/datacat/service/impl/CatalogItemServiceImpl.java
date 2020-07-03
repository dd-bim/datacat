package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.CatalogItem;
import de.bentrm.datacat.domain.Facet;
import de.bentrm.datacat.domain.Translation;
import de.bentrm.datacat.graphql.dto.EntityInput;
import de.bentrm.datacat.graphql.dto.EntityUpdateInput;
import de.bentrm.datacat.graphql.dto.InputMapper;
import de.bentrm.datacat.graphql.dto.TextInput;
import de.bentrm.datacat.repository.FacetRepository;
import de.bentrm.datacat.repository.GraphEntityRepository;
import de.bentrm.datacat.service.CrudEntityService;
import de.bentrm.datacat.service.RelGroupsService;
import de.bentrm.datacat.service.Specification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static de.bentrm.datacat.service.impl.ServiceUtil.mapTextInputToTranslationSet;

@Validated
@Transactional(readOnly = true)
public abstract class CatalogItemServiceImpl<
        T extends CatalogItem,
        C extends EntityInput,
        U extends EntityUpdateInput,
        R extends GraphEntityRepository<T>>
        implements CrudEntityService<T, C, U> {

    Logger logger = LoggerFactory.getLogger(SubjectServiceImpl.class);

    protected final R repository;

    @Autowired
    @Lazy
    protected RelGroupsService relGroupsService;

    @Autowired
    private FacetRepository facetRepository;

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
            final Translation translation = inputMapper.toTranslation(name);
            entity.getNames().add(translation);
        }

        final Specification specification = Specification
                .unspecified()
                .setIdIn(dto.getFacets());
        final Page<Facet> all = facetRepository.findAll(specification);
        entity.getFacets().addAll(all.getContent());
    }

    @Transactional
    @Override
    public T update(@Valid U dto) {
        T entity = repository
                .findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("No Object with id " + dto.getId() + " found."));

        logger.debug("Updating entity {}", entity);
        updateEntityProperties(entity, dto);
        logger.debug("New state {}", entity);

        return repository.save(entity);
    }

    protected void updateEntityProperties(T entity, U dto) {
        final Set<Translation> persistentNamesSet = entity.getNames();
        final List<TextInput> textInput = dto.getNames();

        mapTextInputToTranslationSet(persistentNamesSet, textInput);

        final Specification specification = Specification
                .unspecified()
                .setIdIn(dto.getFacets());
        final Page<Facet> all = facetRepository.findAll(specification);
        entity.getFacets().retainAll(all.getContent());
        entity.getFacets().addAll(all.getContent());
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
    public @NotNull Page<T> findByIds(@NotNull List<String> ids, @NotNull Pageable pageable) {
        final Specification spec = Specification.unspecified();
        spec.setIdIn(ids);
        spec.setPageNumber(pageable.getPageNumber());
        spec.setPageSize(pageable.getPageSize());
        return repository.findAll(spec);
    }

    @Override
    public @NotNull Page<T> search(@NotNull Specification specification) {
        return repository.findAll(specification);
    }

    protected abstract T newEntityInstance();
}
