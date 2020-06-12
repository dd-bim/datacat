package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.DomainValueRelationship;
import de.bentrm.datacat.domain.XtdMeasureWithUnit;
import de.bentrm.datacat.domain.XtdUnit;
import de.bentrm.datacat.domain.XtdValue;
import de.bentrm.datacat.graphql.dto.IdInput;
import de.bentrm.datacat.graphql.dto.ItemListUpdateInput;
import de.bentrm.datacat.graphql.dto.MeasureInput;
import de.bentrm.datacat.graphql.dto.MeasureUpdateInput;
import de.bentrm.datacat.repository.MeasureWithUnitRepository;
import de.bentrm.datacat.repository.UnitRepository;
import de.bentrm.datacat.repository.ValueRepository;
import de.bentrm.datacat.service.MeasureWithUnitService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Validated
@Transactional(readOnly = true)
public class MeasureWithUnitServiceImpl
        extends CrudRootServiceImpl<XtdMeasureWithUnit, MeasureInput, MeasureUpdateInput, MeasureWithUnitRepository>
        implements MeasureWithUnitService {

    private final UnitRepository unitRepository;

    private final ValueRepository valueRepository;

    public MeasureWithUnitServiceImpl(MeasureWithUnitRepository repository,
                                      UnitRepository unitRepository,
                                      ValueRepository valueRepository) {
        super(repository);
        this.unitRepository = unitRepository;
        this.valueRepository = valueRepository;
    }

    protected XtdMeasureWithUnit newEntityInstance() {
        return new XtdMeasureWithUnit();
    }

    @Override
    protected void setEntityProperties(XtdMeasureWithUnit entity, MeasureInput dto) {
        super.setEntityProperties(entity, dto);

        final String unitComponentId = dto.getUnit();
        if (unitComponentId != null) {
            setUnitComponent(entity, unitComponentId);
        }

        List<String> add = dto.getValueDomain().getAdd();
        for (int i = 0; i < add.size(); i++) {
            String valueId = add.get(i);
            final Optional<XtdValue> optionalValue = valueRepository.findById(valueId);
            int sortOrder = i;
            optionalValue.ifPresentOrElse(value -> {
                DomainValueRelationship relationship = new DomainValueRelationship();
                relationship.setMeasure(entity);
                relationship.setValue(value);
                relationship.setSortOrder(sortOrder);

                entity.getValueDomain().add(relationship);
            }, () -> {
                throw new IllegalArgumentException(String.format("No value with id %s found.", valueId));
            });
        }
    }

    @Override
    protected void updateEntityProperties(XtdMeasureWithUnit entity, MeasureUpdateInput dto) {
        super.updateEntityProperties(entity, dto);

        final SortedSet<DomainValueRelationship> persistentValueDomain = entity.getValueDomain();
        final ItemListUpdateInput valueDomainInput = dto.getValueDomain();
        final List<IdInput> inserts = valueDomainInput.getInsert();

        // update unit component
        final String unitComponentId = dto.getUnit();
        if (unitComponentId != null) {
            setUnitComponent(entity, unitComponentId);
        } else {
            entity.setUnitComponent(null);
        }

        // remove value domain entries
        for (String id : valueDomainInput.getRemove()) {
            final boolean removed = persistentValueDomain.removeIf(value -> value.getId().equals(id));
            if (!removed) {
                throw new IllegalArgumentException(String.format("No value with id %s found in value domain.", id));
            }
        }

        // Guard against double inserts
        final List<String> preexistingIds = persistentValueDomain
                .stream()
                .map(x -> x.getValue().getId())
                .collect(Collectors.toList());
        final Set<String> intersections = inserts
                .stream()
                .map(IdInput::getId)
                .filter(preexistingIds::contains)
                .collect(Collectors.toSet());
        if (!intersections.isEmpty()) {
            throw new IllegalArgumentException(String.format("The following values are already present in the value domain: %s", intersections.toString()));
        }

        // Update sort order of preexisting items
        List<DomainValueRelationship> relationships = new ArrayList<>(persistentValueDomain);
        for (IdInput insert : inserts) {
            for (DomainValueRelationship relationship : relationships) {
                final int sortOrder = relationship.getSortOrder();
                if (sortOrder >= insert.getIndex()) {
                    relationship.setSortOrder(sortOrder + 1);
                }
            }
        }

        // Initialize new Relationships
        for (IdInput insert : inserts) {
            final Optional<XtdValue> optionalValue = valueRepository.findById(insert.getId());
            optionalValue.ifPresentOrElse(value -> {
                DomainValueRelationship relationship = new DomainValueRelationship();
                relationship.setMeasure(entity);
                relationship.setValue(value);
                relationship.setSortOrder(insert.getIndex());

                relationships.add(insert.getIndex(), relationship);
            }, () -> {
                throw new IllegalArgumentException(String.format("No value with id %s found.", insert.getId()));
            });
        }

        // Update sort order of merged item list
        for (int i = 0; i < relationships.size(); i++) {
            DomainValueRelationship relationship = relationships.get(i);
            relationship.setSortOrder(i);
        }

        // Update entity state
        entity.getValueDomain().clear();
        entity.getValueDomain().addAll(relationships);
    }

    private void setUnitComponent(XtdMeasureWithUnit entity, String unitComponentId) {
        final Optional<XtdUnit> unit = unitRepository.findById(unitComponentId);
        unit.ifPresentOrElse(entity::setUnitComponent, () -> {
            throw new IllegalArgumentException(String.format("No unit with id %s found.", unitComponentId));
        });
    }
}
