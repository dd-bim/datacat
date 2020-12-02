package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdRelActsUpon;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import de.bentrm.datacat.catalog.repository.RelActsUponRepository;
import de.bentrm.datacat.catalog.service.ActsUponService;
import de.bentrm.datacat.catalog.service.EntityMapper;
import de.bentrm.datacat.catalog.service.value.OneToManyRelationshipValue;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Service
@Validated
@Transactional(readOnly = true)
public class ActsUponServiceImpl extends AbstractServiceImpl<XtdRelActsUpon> implements ActsUponService {

    private final EntityMapper entityMapper = EntityMapper.INSTANCE;
    private final EntityRepository<XtdRoot> rootRepository;

    public ActsUponServiceImpl(SessionFactory sessionFactory, RelActsUponRepository repository,
                               EntityRepository<XtdRoot> rootRepository) {
        super(XtdRelActsUpon.class, sessionFactory, repository);
        this.rootRepository = rootRepository;
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

        return getRepository().save(ref);
    }
}
