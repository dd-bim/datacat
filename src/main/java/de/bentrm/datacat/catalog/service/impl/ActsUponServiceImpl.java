package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.specification.QuerySpecification;
import de.bentrm.datacat.catalog.domain.XtdRelActsUpon;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import de.bentrm.datacat.catalog.repository.RelActsUponRepository;
import de.bentrm.datacat.catalog.repository.RootRepository;
import de.bentrm.datacat.catalog.service.ActsUponService;
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
public class ActsUponServiceImpl implements ActsUponService {

    private final EntityMapper entityMapper = EntityMapper.INSTANCE;
    private final RelActsUponRepository actsUponRepository;
    private final RootRepository rootRepository;

    private final QueryDelegate<XtdRelActsUpon> queryDelegate;

    public ActsUponServiceImpl(RelActsUponRepository repository, RootRepository rootRepository) {
        this.actsUponRepository = repository;
        this.rootRepository = rootRepository;
        this.queryDelegate = new QueryDelegate<>(repository);
    }

    @Transactional
    @Override
    public @NotNull XtdRelActsUpon create(OneToManyRelationshipValue value) {
        final XtdRelActsUpon ref = new XtdRelActsUpon();

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

        return actsUponRepository.save(ref);
    }

    @Override
    public @NotNull Optional<XtdRelActsUpon> findById(@NotNull String id) {
        return queryDelegate.findById(id);
    }

    @Override
    public @NotNull List<XtdRelActsUpon> findAllByIds(@NotNull List<String> ids) {
        return queryDelegate.findAllByIds(ids);
    }

    @Override
    public @NotNull Page<XtdRelActsUpon> findAll(@NotNull QuerySpecification specification) {
        return queryDelegate.findAll(specification);
    }

    @Override
    public @NotNull long count(@NotNull QuerySpecification specification) {
        return queryDelegate.count(specification);
    }
}
