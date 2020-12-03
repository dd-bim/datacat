package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdMeasureWithUnit;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdRelAssignsMeasures;
import de.bentrm.datacat.catalog.repository.AssignsMeasuresRepository;
import de.bentrm.datacat.catalog.repository.MeasureRepository;
import de.bentrm.datacat.catalog.repository.PropertyRepository;
import de.bentrm.datacat.catalog.service.AssignsMeasuresService;
import de.bentrm.datacat.catalog.service.EntityMapper;
import de.bentrm.datacat.catalog.service.value.OneToManyRelationshipValue;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Validated
@Transactional(readOnly = true)
public class AssignsMeasuresServiceImpl extends AbstractServiceImpl<XtdRelAssignsMeasures>
        implements AssignsMeasuresService {

    private final EntityMapper entityMapper = EntityMapper.INSTANCE;
    private final EntityRepository<XtdProperty> propertyRepository;
    private final EntityRepository<XtdMeasureWithUnit> measureWithUnitRepository;

    public AssignsMeasuresServiceImpl(SessionFactory sessionFactory,
                                      AssignsMeasuresRepository repository,
                                      PropertyRepository propertyRepository,
                                      MeasureRepository measureRepository) {
        super(XtdRelAssignsMeasures.class, sessionFactory, repository);
        this.propertyRepository = propertyRepository;
        this.measureWithUnitRepository = measureRepository;
    }

    @Transactional
    @Override
    public @NotNull XtdRelAssignsMeasures create(OneToManyRelationshipValue value) {
        log.trace("Creating new XtdRelAssignsMeasures relationship from value: {}", value);

        final XtdRelAssignsMeasures relation = new XtdRelAssignsMeasures();
        entityMapper.setProperties(value, relation);
        log.trace("Mapped provided values to entity: {}", relation);

        final XtdProperty relating = propertyRepository.findById(value.getFrom()).orElseThrow();
        log.trace("Setting relating side: {}", relating);
        relation.setRelatingProperty(relating);

        final Iterable<XtdMeasureWithUnit> things = measureWithUnitRepository.findAllById(value.getTo());
        final List<XtdMeasureWithUnit> related = new ArrayList<>();
        things.forEach(related::add);
        log.trace("Setting related side: {}", related);
        Assert.notEmpty(related, "A relationship must have at least on related element.");

        relation.getRelatedMeasures().addAll(related);

        return getRepository().save(relation);
    }
}
