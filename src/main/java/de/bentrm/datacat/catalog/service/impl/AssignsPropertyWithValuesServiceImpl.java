package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdRelAssignsPropertyWithValues;
import de.bentrm.datacat.catalog.service.AssignsPropertyWithValuesService;
import de.bentrm.datacat.catalog.service.EntityMapper;
import de.bentrm.datacat.catalog.service.value.QualifiedOneToManyRelationshipValue;
import org.apache.commons.lang3.NotImplementedException;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Service
@Validated
@Transactional(readOnly = true)
public class AssignsPropertyWithValuesServiceImpl extends AbstractServiceImpl<XtdRelAssignsPropertyWithValues> implements AssignsPropertyWithValuesService {

    private final EntityMapper entityMapper = EntityMapper.INSTANCE;
    private final EntityRepository<XtdObject> objectRepository;
    private final EntityRepository<XtdProperty> propertyRepository;

    public AssignsPropertyWithValuesServiceImpl(SessionFactory sessionFactory, EntityRepository<XtdRelAssignsPropertyWithValues> repository,
                                                EntityRepository<XtdObject> objectRepository,
                                                EntityRepository<XtdProperty> propertyRepository) {
        super(XtdRelAssignsPropertyWithValues.class, sessionFactory, repository);
        this.objectRepository = objectRepository;
        this.propertyRepository = propertyRepository;
    }

    // TODO
    @Transactional
    @Override
    public @NotNull XtdRelAssignsPropertyWithValues create(QualifiedOneToManyRelationshipValue value) {
        final XtdRelAssignsPropertyWithValues relation = new XtdRelAssignsPropertyWithValues();

        throw new NotImplementedException();
//        entityMapper.setProperties(value, relation);
//
//        final XtdObject relating = objectRepository.findById(value.getFrom()).orElseThrow();
//        relation.setRelatingObject(relating);
//
//        final Iterable<XtdProperty> things = propertyRepository.findAllById(value.getTo());
//        final List<XtdProperty> related = new ArrayList<>();
//        things.forEach(related::add);
//        if (related.isEmpty()) {
//            throw new IllegalArgumentException("A relationship must have at least one related member.");
//        }
//        relation.getRelatedProperties().addAll(related);
//
//        return assignsCollectionsRepository.save(relation);
    }
}
