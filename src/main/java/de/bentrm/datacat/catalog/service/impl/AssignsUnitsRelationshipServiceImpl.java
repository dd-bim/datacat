package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdMeasureWithUnit;
import de.bentrm.datacat.catalog.domain.XtdRelAssignsUnits;
import de.bentrm.datacat.catalog.domain.XtdUnit;
import de.bentrm.datacat.catalog.service.AssignsUnitsRelationshipService;
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
public class AssignsUnitsRelationshipServiceImpl extends AbstractServiceImpl<XtdRelAssignsUnits> implements AssignsUnitsRelationshipService {

    private final EntityMapper entityMapper = EntityMapper.INSTANCE;
    private final EntityRepository<XtdMeasureWithUnit> measureWithUnitRepository;
    private final EntityRepository<XtdUnit> unitRepository;

    public AssignsUnitsRelationshipServiceImpl(SessionFactory sessionFactory,
                                               EntityRepository<XtdRelAssignsUnits> repository,
                                               EntityRepository<XtdMeasureWithUnit> measureWithUnitRepository,
                                               EntityRepository<XtdUnit> unitRepository) {
        super(XtdRelAssignsUnits.class, sessionFactory, repository);
        this.measureWithUnitRepository = measureWithUnitRepository;
        this.unitRepository = unitRepository;
    }

    @Transactional
    @Override
    public @NotNull XtdRelAssignsUnits create(OneToManyRelationshipValue value) {
        final XtdRelAssignsUnits relation = new XtdRelAssignsUnits();

        entityMapper.setProperties(value, relation);

        final XtdMeasureWithUnit relating = measureWithUnitRepository.findById(value.getFrom()).orElseThrow();
        relation.setRelatingMeasure(relating);

        final Iterable<XtdUnit> things = unitRepository.findAllById(value.getTo());
        final List<XtdUnit> related = new ArrayList<>();
        things.forEach(related::add);
        if (related.isEmpty()) {
            throw new IllegalArgumentException("A relationship must have at least one related member.");
        }
        relation.getRelatedUnits().addAll(related);

        return getRepository().save(relation);
    }
}
