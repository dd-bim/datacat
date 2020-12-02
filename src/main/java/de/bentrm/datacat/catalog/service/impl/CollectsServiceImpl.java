package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.XtdCollection;
import de.bentrm.datacat.catalog.domain.XtdRelCollects;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import de.bentrm.datacat.catalog.repository.CollectionRepository;
import de.bentrm.datacat.catalog.repository.RelCollectsRepository;
import de.bentrm.datacat.catalog.repository.RootRepository;
import de.bentrm.datacat.catalog.service.CollectsService;
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
public class CollectsServiceImpl extends AbstractServiceImpl<XtdRelCollects> implements CollectsService {

    private final EntityMapper entityMapper = EntityMapper.INSTANCE;
    private final CollectionRepository collectionRepository;
    private final RootRepository rootRepository;

    public CollectsServiceImpl(SessionFactory sessionFactory,
                               RelCollectsRepository repository,
                               CollectionRepository collectionRepository,
                               RootRepository rootRepository) {
        super(XtdRelCollects.class, sessionFactory, repository);
        this.collectionRepository = collectionRepository;
        this.rootRepository = rootRepository;
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

        return getRepository().save(ref);
    }
}
