package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.specification.QuerySpecification;
import de.bentrm.datacat.catalog.domain.XtdRelGroups;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import de.bentrm.datacat.catalog.repository.RelGroupsRepository;
import de.bentrm.datacat.catalog.repository.RootRepository;
import de.bentrm.datacat.catalog.service.EntityMapper;
import de.bentrm.datacat.catalog.service.GroupsService;
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
public class GroupsServiceImpl implements GroupsService {

    private final EntityMapper entityMapper = EntityMapper.INSTANCE;
    private final RelGroupsRepository groupsRepository;
    private final RootRepository rootRepository;

    private final QueryServiceDelegate<XtdRelGroups> queryDelegate;

    public GroupsServiceImpl(RelGroupsRepository repository, RootRepository rootRepository) {
        this.groupsRepository = repository;
        this.rootRepository = rootRepository;
        this.queryDelegate = new QueryServiceDelegate<>(repository);
    }

    @Transactional
    @Override
    public @NotNull XtdRelGroups create(OneToManyRelationshipValue value) {
        final XtdRelGroups ref = new XtdRelGroups();

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

        return groupsRepository.save(ref);
    }

    @Override
    public @NotNull Optional<XtdRelGroups> findById(@NotNull String id) {
        return queryDelegate.findById(id);
    }

    @Override
    public @NotNull List<XtdRelGroups> findAllByIds(@NotNull List<String> ids) {
        return queryDelegate.findAllByIds(ids);
    }

    @Override
    public @NotNull Page<XtdRelGroups> findAll(@NotNull QuerySpecification specification) {
        return queryDelegate.findAll(specification);
    }

    @Override
    public @NotNull long count(@NotNull QuerySpecification specification) {
        return queryDelegate.count(specification);
    }
}
