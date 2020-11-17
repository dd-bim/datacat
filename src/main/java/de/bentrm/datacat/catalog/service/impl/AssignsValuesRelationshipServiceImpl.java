package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.specification.QuerySpecification;
import de.bentrm.datacat.catalog.domain.XtdMeasureWithUnit;
import de.bentrm.datacat.catalog.domain.XtdRelAssignsValues;
import de.bentrm.datacat.catalog.domain.XtdValue;
import de.bentrm.datacat.catalog.repository.AssignsValuesRelationshipRepository;
import de.bentrm.datacat.catalog.repository.MeasureWithUnitRepository;
import de.bentrm.datacat.catalog.repository.ValueRepository;
import de.bentrm.datacat.catalog.service.AssignsValuesRelationshipService;
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
public class AssignsValuesRelationshipServiceImpl implements AssignsValuesRelationshipService {

    private final EntityMapper entityMapper = EntityMapper.INSTANCE;
    private final AssignsValuesRelationshipRepository assignsValuesRelationshipRepository;
    private final MeasureWithUnitRepository measureWithUnitRepository;
    private final ValueRepository valueRepository;

    private final QueryDelegate<XtdRelAssignsValues> queryDelegate;

    public AssignsValuesRelationshipServiceImpl(AssignsValuesRelationshipRepository repository,
                                                MeasureWithUnitRepository measureWithUnitRepository,
                                                ValueRepository valueRepository) {
        this.assignsValuesRelationshipRepository = repository;
        this.measureWithUnitRepository = measureWithUnitRepository;
        this.valueRepository = valueRepository;
        this.queryDelegate = new QueryDelegate<>(repository);
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

        return assignsValuesRelationshipRepository.save(relation);
    }

    @Override
    public @NotNull Optional<XtdRelAssignsValues> findById(@NotNull String id) {
        return queryDelegate.findById(id);
    }

    @Override
    public @NotNull List<XtdRelAssignsValues> findAllByIds(@NotNull List<String> ids) {
        return queryDelegate.findAllByIds(ids);
    }

    @Override
    public @NotNull Page<XtdRelAssignsValues> findAll(@NotNull QuerySpecification specification) {
        return queryDelegate.findAll(specification);
    }

    @Override
    public @NotNull long count(@NotNull QuerySpecification specification) {
        return queryDelegate.count(specification);
    }
}
