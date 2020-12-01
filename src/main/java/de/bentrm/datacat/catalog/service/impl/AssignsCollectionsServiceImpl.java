package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdCollection;
import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.domain.XtdRelAssignsCollections;
import de.bentrm.datacat.catalog.service.AssignsCollectionsService;
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
public class AssignsCollectionsServiceImpl extends AbstractServiceImpl<XtdRelAssignsCollections> implements AssignsCollectionsService {

    private final EntityMapper entityMapper = EntityMapper.INSTANCE;
    private final EntityRepository<XtdObject> objectRepository;
    private final EntityRepository<XtdCollection> collectionRepository;

    public AssignsCollectionsServiceImpl(SessionFactory sessionFactory, EntityRepository<XtdRelAssignsCollections> repository,
                                         EntityRepository<XtdObject> objectRepository, EntityRepository<XtdCollection> collectionRepository) {
        super(XtdRelAssignsCollections.class, sessionFactory, repository);
        this.objectRepository = objectRepository;
        this.collectionRepository = collectionRepository;
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

        return getRepository().save(relation);
    }
}
