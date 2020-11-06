package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.specification.QuerySpecification;
import de.bentrm.datacat.catalog.domain.XtdCollection;
import de.bentrm.datacat.catalog.domain.XtdRelCollects;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import de.bentrm.datacat.catalog.repository.CollectionRepository;
import de.bentrm.datacat.catalog.repository.RelCollectsRepository;
import de.bentrm.datacat.catalog.repository.RootRepository;
import de.bentrm.datacat.catalog.service.CollectsService;
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
public class CollectsServiceImpl implements CollectsService {

    private final EntityMapper entityMapper = EntityMapper.INSTANCE;
    private final RelCollectsRepository collectsRepository;
    private final CollectionRepository collectionRepository;
    private final RootRepository rootRepository;

    private final QueryDelegate<XtdRelCollects> queryDelegate;

    public CollectsServiceImpl(RelCollectsRepository repository, CollectionRepository collectionRepository, RootRepository rootRepository) {
        this.collectsRepository = repository;
        this.collectionRepository = collectionRepository;
        this.rootRepository = rootRepository;
        this.queryDelegate = new QueryDelegate<>(repository);
    }

    @Transactional
    @Override
    public @NotNull XtdRelCollects create(OneToManyRelationshipValue value) {
        final XtdRelCollects ref = new XtdRelCollects();

        entityMapper.setProperties(value, ref);

        final XtdCollection relating = collectionRepository.findById(value.getFrom()).orElseThrow();
        ref.setRelatingCollection(relating);

        final Iterable<XtdRoot> things = rootRepository.findAllById(value.getTo());
        final List<XtdRoot> related = new ArrayList<>();
        things.forEach(related::add);
        if (related.isEmpty()) {
            throw new IllegalArgumentException("A relationship must have at least one related member.");
        }
        ref.getRelatedThings().addAll(related);

        return collectsRepository.save(ref);
    }

    @Override
    public @NotNull Optional<XtdRelCollects> findById(@NotNull String id) {
        return queryDelegate.findById(id);
    }

    @Override
    public @NotNull List<XtdRelCollects> findAllByIds(@NotNull List<String> ids) {
        return queryDelegate.findAllByIds(ids);
    }

    @Override
    public @NotNull Page<XtdRelCollects> findAll(@NotNull QuerySpecification specification) {
        return queryDelegate.findAll(specification);
    }

    @Override
    public @NotNull long count(@NotNull QuerySpecification specification) {
        return queryDelegate.count(specification);
    }
}
