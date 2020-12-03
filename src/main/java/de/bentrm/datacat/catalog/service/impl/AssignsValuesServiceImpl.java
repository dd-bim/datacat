package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.XtdMeasureWithUnit;
import de.bentrm.datacat.catalog.domain.XtdRelAssignsValues;
import de.bentrm.datacat.catalog.domain.XtdValue;
import de.bentrm.datacat.catalog.repository.AssignsValuesRepository;
import de.bentrm.datacat.catalog.repository.MeasureRepository;
import de.bentrm.datacat.catalog.repository.ValueRepository;
import de.bentrm.datacat.catalog.service.AssignsValuesService;
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
public class AssignsValuesServiceImpl extends AbstractServiceImpl<XtdRelAssignsValues> implements AssignsValuesService {

    private final EntityMapper entityMapper = EntityMapper.INSTANCE;
    private final MeasureRepository measureWithUnitRepository;
    private final ValueRepository valueRepository;

    public AssignsValuesServiceImpl(SessionFactory sessionFactory,
                                    AssignsValuesRepository repository,
                                    MeasureRepository measureWithUnitRepository,
                                    ValueRepository valueRepository) {
        super(XtdRelAssignsValues.class, sessionFactory, repository);
        this.measureWithUnitRepository = measureWithUnitRepository;
        this.valueRepository = valueRepository;
    }

    @Transactional
    @Override
    public @NotNull XtdRelAssignsValues create(OneToManyRelationshipValue value) {
        final XtdRelAssignsValues relation = new XtdRelAssignsValues();

        entityMapper.setProperties(value, relation);

        final XtdMeasureWithUnit relating = measureWithUnitRepository.findById(value.getFrom()).orElseThrow();
        relation.setRelatingMeasure(relating);

        final Iterable<XtdValue> things = valueRepository.findAllById(value.getTo());
        final List<XtdValue> related = new ArrayList<>();
        things.forEach(related::add);
        if (related.isEmpty()) {
            throw new IllegalArgumentException("A relationship must have at least one related member.");
        }
        relation.getRelatedValues().addAll(related);

        return getRepository().save(relation);
    }
}
