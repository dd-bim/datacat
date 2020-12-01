package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdMeasureWithUnit;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdRelAssignsMeasures;
import de.bentrm.datacat.catalog.service.AssignsMeasuresRelationshipService;
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
public class AssignsMeasuresRelationshipServiceImpl extends AbstractServiceImpl<XtdRelAssignsMeasures>
        implements AssignsMeasuresRelationshipService {

    private final EntityMapper entityMapper = EntityMapper.INSTANCE;
    private final EntityRepository<XtdProperty> propertyRepository;
    private final EntityRepository<XtdMeasureWithUnit> measureWithUnitRepository;

    public AssignsMeasuresRelationshipServiceImpl(SessionFactory sessionFactory, EntityRepository<XtdRelAssignsMeasures> repository,
                                                  EntityRepository<XtdProperty> propertyRepository,
                                                  EntityRepository<XtdMeasureWithUnit> measureWithUnitRepository) {
        super(XtdRelAssignsMeasures.class, sessionFactory, repository);
        this.propertyRepository = propertyRepository;
        this.measureWithUnitRepository = measureWithUnitRepository;
    }

    @Transactional
    @Override
    public @NotNull XtdRelAssignsMeasures create(OneToManyRelationshipValue value) {
        final XtdRelAssignsMeasures relation = new XtdRelAssignsMeasures();

        entityMapper.setProperties(value, relation);

        final XtdProperty relating = propertyRepository.findById(value.getFrom()).orElseThrow();
        relation.setRelatingProperty(relating);

        final Iterable<XtdMeasureWithUnit> things = measureWithUnitRepository.findAllById(value.getTo());
        final List<XtdMeasureWithUnit> related = new ArrayList<>();
        things.forEach(related::add);
        if (related.isEmpty()) {
            throw new IllegalArgumentException("A relationship must have at least one related member.");
        }
        relation.getRelatedMeasures().addAll(related);

        return getRepository().save(relation);
    }
}
