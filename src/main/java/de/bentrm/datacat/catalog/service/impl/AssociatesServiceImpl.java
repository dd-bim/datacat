package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdRelAssociates;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import de.bentrm.datacat.catalog.service.AssociatesService;
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
public class AssociatesServiceImpl extends AbstractServiceImpl<XtdRelAssociates> implements AssociatesService {

    private final EntityMapper entityMapper = EntityMapper.INSTANCE;
    private final EntityRepository<XtdRelAssociates> associatesRepository;
    private final EntityRepository<XtdRoot> rootRepository;

    public AssociatesServiceImpl(SessionFactory sessionFactory,
                                 EntityRepository<XtdRelAssociates> repository,
                                 EntityRepository<XtdRoot> rootRepository) {
        super(XtdRelAssociates.class, sessionFactory, repository);
        this.associatesRepository = repository;
        this.rootRepository = rootRepository;
    }

    @Transactional
    @Override
    public @NotNull XtdRelAssociates create(OneToManyRelationshipValue value) {
        final XtdRelAssociates ref = new XtdRelAssociates();

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

        return associatesRepository.save(ref);
    }
}
