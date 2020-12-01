package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdRelAssignsProperties;
import de.bentrm.datacat.catalog.service.AssignsPropertiesService;
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
public class AssignsPropertiesServiceImpl extends AbstractServiceImpl<XtdRelAssignsProperties> implements AssignsPropertiesService {

    private final EntityMapper entityMapper = EntityMapper.INSTANCE;
    private final EntityRepository<XtdObject> objectRepository;
    private final EntityRepository<XtdProperty> propertyRepository;

    public AssignsPropertiesServiceImpl(SessionFactory sessionFactory, EntityRepository<XtdRelAssignsProperties> repository,
                                        EntityRepository<XtdObject> objectRepository,
                                        EntityRepository<XtdProperty> propertyRepository) {
        super(XtdRelAssignsProperties.class, sessionFactory, repository);
        this.objectRepository = objectRepository;
        this.propertyRepository = propertyRepository;
    }

    @Transactional
    @Override
    public @NotNull XtdRelAssignsProperties create(OneToManyRelationshipValue value) {
        final XtdRelAssignsProperties relation = new XtdRelAssignsProperties();

        entityMapper.setProperties(value, relation);

        final XtdObject relating = objectRepository.findById(value.getFrom()).orElseThrow();
        relation.setRelatingObject(relating);

        final Iterable<XtdProperty> things = propertyRepository.findAllById(value.getTo());
        final List<XtdProperty> related = new ArrayList<>();
        things.forEach(related::add);
        if (related.isEmpty()) {
            throw new IllegalArgumentException("A relationship must have at least one related member.");
        }
        relation.getRelatedProperties().addAll(related);

        return getRepository().save(relation);
    }
}
