package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.specification.QuerySpecification;
import de.bentrm.datacat.catalog.domain.XtdMeasureWithUnit;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdRelAssignsMeasures;
import de.bentrm.datacat.catalog.repository.AssignsMeasuresRelationshipRepository;
import de.bentrm.datacat.catalog.repository.MeasureWithUnitRepository;
import de.bentrm.datacat.catalog.repository.PropertyRepository;
import de.bentrm.datacat.catalog.service.AssignsMeasuresRelationshipService;
import de.bentrm.datacat.catalog.service.EntityMapper;
import de.bentrm.datacat.catalog.service.value.OneToManyRelationshipValue;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Validated
@Transactional(readOnly = true)
public class AssignsMeasuresRelationshipServiceImpl implements AssignsMeasuresRelationshipService {

    private final EntityMapper entityMapper = EntityMapper.INSTANCE;
    private final AssignsMeasuresRelationshipRepository assignsMeasuresRelationshipRepository;
    private final PropertyRepository propertyRepository;
    private final MeasureWithUnitRepository measureWithUnitRepository;

    private final QueryDelegate<XtdRelAssignsMeasures> queryDelegate;

    public AssignsMeasuresRelationshipServiceImpl(AssignsMeasuresRelationshipRepository repository,
                                                  PropertyRepository propertyRepository,
                                                  MeasureWithUnitRepository measureWithUnitRepository) {
        this.assignsMeasuresRelationshipRepository = repository;
        this.propertyRepository = propertyRepository;
        this.measureWithUnitRepository = measureWithUnitRepository;
        this.queryDelegate = new QueryDelegate<>(repository);
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

        return assignsMeasuresRelationshipRepository.save(relation);
    }

    @Override
    public @NotNull Optional<XtdRelAssignsMeasures> findById(@NotNull String id) {
        return queryDelegate.findById(id);
    }

    @Override
    public @NotNull List<XtdRelAssignsMeasures> findAllByIds(@NotNull List<String> ids) {
        return queryDelegate.findAllByIds(ids);
    }

    @Override
    public @NotNull Page<XtdRelAssignsMeasures> findAll(@NotNull QuerySpecification specification) {
        return queryDelegate.findAll(specification);
    }

    @Override
    public @NotNull long count(@NotNull QuerySpecification specification) {
        return queryDelegate.count(specification);
    }
}
