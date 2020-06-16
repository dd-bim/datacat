package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.DomainValueRelationship;
import de.bentrm.datacat.domain.XtdMeasureWithUnit;
import de.bentrm.datacat.domain.XtdUnit;
import de.bentrm.datacat.domain.XtdValue;
import de.bentrm.datacat.graphql.dto.MeasureInput;
import de.bentrm.datacat.graphql.dto.MeasureUpdateInput;
import de.bentrm.datacat.repository.MeasureWithUnitRepository;
import de.bentrm.datacat.repository.UnitRepository;
import de.bentrm.datacat.repository.ValueRepository;
import de.bentrm.datacat.service.MeasureWithUnitService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

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

        final String unitComponentId = dto.getUnitComponent();
        if (unitComponentId != null) {
            setUnitComponent(entity, unitComponentId);
        }

        List<String> add = dto.getValueDomain();
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

        // update unit component
        final String unitComponentId = dto.getUnitComponent();
        if (unitComponentId != null) {
            setUnitComponent(entity, unitComponentId);
        } else {
            entity.setUnitComponent(null);
        }

        final SortedSet<DomainValueRelationship> persistentValueDomain = entity.getValueDomain();
        final List<String> valueDomain = dto.getValueDomain();

        SortedSet<DomainValueRelationship> newMapping = new TreeSet<>();
        for (int i = 0; i < valueDomain.size(); i++) {
            final String id = valueDomain.get(i);
            final int sortOrder = i;

            final Optional<DomainValueRelationship> relationship = persistentValueDomain.stream()
                    .filter(x -> x.getValue().getId().equals(id))
                    .findFirst();
            if (relationship.isPresent()) {
                final DomainValueRelationship preExistingRelationship = relationship.get();
                preExistingRelationship.setSortOrder(sortOrder);
                newMapping.add(preExistingRelationship);
            } else {
                valueRepository
                        .findById(id)
                        .ifPresentOrElse(value -> {
                            DomainValueRelationship newRelationship = new DomainValueRelationship();
                            newRelationship.setMeasure(entity);
                            newRelationship.setValue(value);
                            newRelationship.setSortOrder(sortOrder);
                            newMapping.add(newRelationship);
                        }, () -> {
                            throw new IllegalArgumentException(String.format("No value with id %s found.", id));
                        });
            }
        }

        entity.getValueDomain().clear();
        entity.getValueDomain().addAll(newMapping);
    }

    private void setUnitComponent(XtdMeasureWithUnit entity, String unitComponentId) {
        final Optional<XtdUnit> unit = unitRepository.findById(unitComponentId);
        unit.ifPresentOrElse(entity::setUnitComponent, () -> {
            throw new IllegalArgumentException(String.format("No unit with id %s found.", unitComponentId));
        });
    }
}
