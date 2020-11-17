package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.specification.QuerySpecification;
import de.bentrm.datacat.catalog.domain.XtdMeasureWithUnit;
import de.bentrm.datacat.catalog.domain.XtdRelAssignsUnits;
import de.bentrm.datacat.catalog.domain.XtdUnit;
import de.bentrm.datacat.catalog.repository.AssignsUnitsRelationshipRepository;
import de.bentrm.datacat.catalog.repository.MeasureWithUnitRepository;
import de.bentrm.datacat.catalog.repository.UnitRepository;
import de.bentrm.datacat.catalog.service.AssignsUnitsRelationshipService;
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
public class AssignsUnitsRelationshipServiceImpl implements AssignsUnitsRelationshipService {

    private final EntityMapper entityMapper = EntityMapper.INSTANCE;
    private final AssignsUnitsRelationshipRepository assignsUnitsRelationshipRepository;
    private final MeasureWithUnitRepository measureWithUnitRepository;
    private final UnitRepository unitRepository;

    private final QueryDelegate<XtdRelAssignsUnits> queryDelegate;

    public AssignsUnitsRelationshipServiceImpl(AssignsUnitsRelationshipRepository repository,
                                               MeasureWithUnitRepository measureWithUnitRepository,
                                               UnitRepository unitRepository) {
        this.assignsUnitsRelationshipRepository = repository;
        this.measureWithUnitRepository = measureWithUnitRepository;
        this.unitRepository = unitRepository;
        this.queryDelegate = new QueryDelegate<>(repository);
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

        return assignsUnitsRelationshipRepository.save(relation);
    }

    @Override
    public @NotNull Optional<XtdRelAssignsUnits> findById(@NotNull String id) {
        return queryDelegate.findById(id);
    }

    @Override
    public @NotNull List<XtdRelAssignsUnits> findAllByIds(@NotNull List<String> ids) {
        return queryDelegate.findAllByIds(ids);
    }

    @Override
    public @NotNull Page<XtdRelAssignsUnits> findAll(@NotNull QuerySpecification specification) {
        return queryDelegate.findAll(specification);
    }

    @Override
    public @NotNull long count(@NotNull QuerySpecification specification) {
        return queryDelegate.count(specification);
    }
}
