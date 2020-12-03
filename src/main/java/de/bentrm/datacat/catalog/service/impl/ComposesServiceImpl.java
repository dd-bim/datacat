package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.XtdRelComposes;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import de.bentrm.datacat.catalog.repository.ComposesRepository;
import de.bentrm.datacat.catalog.repository.RootRepository;
import de.bentrm.datacat.catalog.service.ComposesService;
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
public class ComposesServiceImpl extends AbstractServiceImpl<XtdRelComposes> implements ComposesService {

    private final EntityMapper entityMapper = EntityMapper.INSTANCE;
    private final RootRepository rootRepository;

    public ComposesServiceImpl(SessionFactory sessionFactory,
                               ComposesRepository repository,
                               RootRepository rootRepository) {
        super(XtdRelComposes.class, sessionFactory, repository);
        this.rootRepository = rootRepository;
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

        return getRepository().save(ref);
    }
}
