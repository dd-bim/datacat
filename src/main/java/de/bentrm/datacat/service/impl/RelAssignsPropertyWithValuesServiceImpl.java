package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.Entity;
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

import java.util.List;
import java.util.stream.Collectors;

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
        final String relatingId = dto.getRelatingObject();
        final String relatedPropertyId = dto.getRelatedProperty();
        final List<String> relatedValuesIds = dto.getRelatedValues();

        super.setEntityProperties(entity, dto);
        mapRelating(entity, relatingId);
        mapRelatedProperty(entity, relatedPropertyId);
        mapRelatedValues(entity, relatedValuesIds);
    }

    @Override
    protected void updateEntityProperties(XtdRelAssignsPropertyWithValues entity, AssignsPropertyWithValuesUpdateInput dto) {
        final List<String> relatedIds = entity.getRelatedValues()
                .stream().map(Entity::getId)
                .collect(Collectors.toList());
        final String newRelatingId = dto.getRelatingObject();
        final String newRelatedPropertyId = dto.getRelatedProperty();
        final List<String> newRelatedValuesIds = dto.getRelatedValues();

        super.updateEntityProperties(entity, dto);
        mapRelating(entity, newRelatingId);
        mapRelatedProperty(entity, newRelatedPropertyId);
        entity.getRelatedValues().removeIf(x -> !newRelatedValuesIds.contains(x.getId()));
        newRelatedValuesIds.removeAll(relatedIds);
        mapRelatedValues(entity, newRelatedValuesIds);
    }

    private void mapRelating(XtdRelAssignsPropertyWithValues entity, String relatingId) {
        objectRepository
                .findById(relatingId)
                .ifPresentOrElse(
                        entity::setRelatingObject,
                        ServiceUtil.throwEntityNotFoundException(relatingId)
                );
    }

    private void mapRelatedProperty(XtdRelAssignsPropertyWithValues entity, String relatedId) {
        propertyRepository
                .findById(relatedId)
                .ifPresentOrElse(
                        entity::setRelatedProperty,
                        ServiceUtil.throwEntityNotFoundException(relatedId)
                );
    }

    private void mapRelatedValues(XtdRelAssignsPropertyWithValues entity, List<String> relatedIds) {
        final Specification spec = Specification
                .unspecified()
                .setIdIn(relatedIds);
        final Page<XtdValue> relatedThings = valueRepository.findAll(spec);
        entity.getRelatedValues().addAll(relatedThings.getContent());
    }
}
