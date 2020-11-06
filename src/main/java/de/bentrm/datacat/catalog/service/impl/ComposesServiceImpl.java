package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.specification.QuerySpecification;
import de.bentrm.datacat.catalog.domain.XtdRelComposes;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import de.bentrm.datacat.catalog.repository.RelComposesRepository;
import de.bentrm.datacat.catalog.repository.RootRepository;
import de.bentrm.datacat.catalog.service.ComposesService;
import de.bentrm.datacat.catalog.service.EntityMapper;
import de.bentrm.datacat.catalog.service.value.OneToManyRelationshipValue;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Validated
@Transactional(readOnly = true)
public class ComposesServiceImpl implements ComposesService {

    private final EntityMapper entityMapper = EntityMapper.INSTANCE;
    private final RelComposesRepository composesRepository;
    private final RootRepository rootRepository;

    private final QueryDelegate<XtdRelComposes> queryDelegate;

    public ComposesServiceImpl(RelComposesRepository repository, RootRepository rootRepository) {
        this.composesRepository = repository;
        this.rootRepository = rootRepository;
        this.queryDelegate = new QueryDelegate<>(repository);
    }

    @Transactional
    @Override
    public @NotNull XtdRelComposes create(OneToManyRelationshipValue value) {
        final XtdRelComposes ref = new XtdRelComposes();

        entityMapper.setProperties(value, ref);

        final XtdRoot relating = rootRepository.findById(value.getFrom()).orElseThrow();
        ref.setRelatingThing(relating);

        final Iterable<XtdRoot> things = rootRepository.findAllById(value.getTo());
        final List<XtdRoot> related = new ArrayList<>();
        things.forEach(related::add);
        if (related.isEmpty()) {
            throw new IllegalArgumentException("A relationship must have at least one related member.");
        }
        ref.getRelatedThings().addAll(related);

        return composesRepository.save(ref);
    }

    @Override
    public @NotNull Optional<XtdRelComposes> findById(@NotNull String id) {
        return queryDelegate.findById(id);
    }

    @Override
    public @NotNull List<XtdRelComposes> findAllByIds(@NotNull List<String> ids) {
        return queryDelegate.findAllByIds(ids);
    }

    @Override
    public @NotNull Page<XtdRelComposes> findAll(@NotNull QuerySpecification specification) {
        return queryDelegate.findAll(specification);
    }

    @Override
    public @NotNull long count(@NotNull QuerySpecification specification) {
        return queryDelegate.count(specification);
    }
}
