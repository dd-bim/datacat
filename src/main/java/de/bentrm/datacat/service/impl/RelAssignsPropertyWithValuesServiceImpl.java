package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.XtdObject;
import de.bentrm.datacat.domain.XtdProperty;
import de.bentrm.datacat.domain.XtdValue;
import de.bentrm.datacat.domain.relationship.XtdRelAssignsPropertyWithValues;
import de.bentrm.datacat.graphql.dto.AssignsPropertyWithValuesInput;
import de.bentrm.datacat.graphql.dto.AssignsPropertyWithValuesUpdateInput;
import de.bentrm.datacat.repository.ObjectRepository;
import de.bentrm.datacat.repository.PropertyRepository;
import de.bentrm.datacat.repository.RelAssignsPropertyWithValuesRepository;
import de.bentrm.datacat.repository.ValueRepository;
import de.bentrm.datacat.service.RelAssignsPropertyWithValuesService;
import de.bentrm.datacat.service.Specification;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional(readOnly = true)
public class RelAssignsPropertyWithValuesServiceImpl
        extends CrudRootServiceImpl<XtdRelAssignsPropertyWithValues, AssignsPropertyWithValuesInput, AssignsPropertyWithValuesUpdateInput, RelAssignsPropertyWithValuesRepository>
        implements RelAssignsPropertyWithValuesService {

    private final ObjectRepository objectRepository;
    private final PropertyRepository propertyRepository;
    private final ValueRepository valueRepository;

    public RelAssignsPropertyWithValuesServiceImpl(RelAssignsPropertyWithValuesRepository repository,
                                                   ObjectRepository objectRepository,
                                                   PropertyRepository propertyRepository,
                                                   ValueRepository valueRepository) {
        super(repository);
        this.objectRepository = objectRepository;
        this.propertyRepository = propertyRepository;
        this.valueRepository = valueRepository;
    }

    @Override
    protected XtdRelAssignsPropertyWithValues newEntityInstance() {
        return new XtdRelAssignsPropertyWithValues();
    }

    @Override
    protected void setEntityProperties(XtdRelAssignsPropertyWithValues entity, AssignsPropertyWithValuesInput dto) {
        super.setEntityProperties(entity, dto);

        XtdObject relatingObject = objectRepository
                .findById(dto.getRelatingObject())
                .orElseThrow(() -> new IllegalArgumentException("No Object with id " + dto.getRelatingObject() + " found."));
        entity.setRelatingObject(relatingObject);

        XtdProperty relatedProperty = propertyRepository
                .findById(dto.getRelatedProperty())
                .orElseThrow(() -> new IllegalArgumentException("No property with id " + dto.getRelatedProperty() + " found."));
        entity.setRelatedProperty(relatedProperty);

        Specification spec = Specification.unspecified();
        spec.setIdIn(dto.getRelatedValues());
        spec.setPageSize(1000);
        Page<XtdValue> relatedValues = valueRepository.findAll(spec);
        entity.getRelatedValues().addAll(relatedValues.getContent());
    }

    @Override
    protected void updateEntityProperties(XtdRelAssignsPropertyWithValues entity, AssignsPropertyWithValuesUpdateInput dto) {
        super.updateEntityProperties(entity, dto);

        if (!dto.getRelatingObject().equals(entity.getRelatingObject().getId())) {
            throw new IllegalArgumentException("Relating side of relationship can't be updated. Create a new relationship instead.");
        }

        if (!dto.getRelatedProperty().equals(entity.getRelatedProperty().getId())) {
            throw new IllegalArgumentException("Related property of relationship can't be updated. Create a new relationship instead.");
        }

        // remove things no longer in this relationship
        entity.getRelatedValues().removeIf(thing -> !dto.getRelatedValues().contains(thing.getId()));

        // add new things to this relationship
        Specification spec = Specification.unspecified();
        spec.setIdIn(dto.getRelatedValues());
        spec.setPageSize(1000);
        Page<XtdValue> relatedValues = valueRepository.findAll(spec);
        entity.getRelatedValues().addAll(relatedValues.getContent());
    }
}
