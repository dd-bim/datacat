package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.specification.QuerySpecification;
import de.bentrm.datacat.catalog.domain.XtdCollection;
import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.domain.XtdRelAssignsCollections;
import de.bentrm.datacat.catalog.repository.CollectionRepository;
import de.bentrm.datacat.catalog.repository.ObjectRepository;
import de.bentrm.datacat.catalog.repository.RelAssignsCollectionsRepository;
import de.bentrm.datacat.catalog.service.AssignsCollectionsService;
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
public class AssignsCollectionsServiceImpl implements AssignsCollectionsService {

    private final EntityMapper entityMapper = EntityMapper.INSTANCE;
    private final RelAssignsCollectionsRepository assignsCollectionsRepository;
    private final ObjectRepository objectRepository;
    private final CollectionRepository collectionRepository;

    private final QueryDelegate<XtdRelAssignsCollections> queryDelegate;

    public AssignsCollectionsServiceImpl(RelAssignsCollectionsRepository repository, ObjectRepository objectRepository,
                                         CollectionRepository collectionRepository) {
        this.assignsCollectionsRepository = repository;
        this.objectRepository = objectRepository;
        this.collectionRepository = collectionRepository;
        this.queryDelegate = new QueryDelegate<>(repository);
    }

    @Transactional
    @Override
    public @NotNull XtdRelAssignsCollections create(OneToManyRelationshipValue value) {
        final XtdRelAssignsCollections relation = new XtdRelAssignsCollections();

        entityMapper.setProperties(value, relation);

        final XtdObject relating = objectRepository.findById(value.getFrom()).orElseThrow();
        relation.setRelatingObject(relating);

        final Iterable<XtdCollection> things = collectionRepository.findAllById(value.getTo());
        final List<XtdCollection> related = new ArrayList<>();
        things.forEach(related::add);
        if (related.isEmpty()) {
            throw new IllegalArgumentException("A relationship must have at least one related member.");
        }
        relation.getRelatedCollections().addAll(related);

        return assignsCollectionsRepository.save(relation);
    }

    @Override
    public @NotNull Optional<XtdRelAssignsCollections> findById(@NotNull String id) {
        return queryDelegate.findById(id);
    }

    @Override
    public @NotNull List<XtdRelAssignsCollections> findAllByIds(@NotNull List<String> ids) {
        return queryDelegate.findAllByIds(ids);
    }

    @Override
    public @NotNull Page<XtdRelAssignsCollections> findAll(@NotNull QuerySpecification specification) {
        return queryDelegate.findAll(specification);
    }

    @Override
    public @NotNull long count(@NotNull QuerySpecification specification) {
        return queryDelegate.count(specification);
    }
}
