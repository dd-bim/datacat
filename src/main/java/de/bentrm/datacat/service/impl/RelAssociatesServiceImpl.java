package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.Entity;
import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.domain.relationship.XtdRelAssociates;
import de.bentrm.datacat.graphql.dto.AssociationInput;
import de.bentrm.datacat.graphql.dto.AssociationUpdateInput;
import de.bentrm.datacat.repository.RelAssociatesRepository;
import de.bentrm.datacat.repository.RootRepository;
import de.bentrm.datacat.service.RelAssociatesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
@Transactional(readOnly = true)
public class RelAssociatesServiceImpl
        extends CrudRootServiceImpl<XtdRelAssociates, AssociationInput, AssociationUpdateInput, RelAssociatesRepository>
        implements RelAssociatesService {

    private final RootRepository thingRepository;

    public RelAssociatesServiceImpl(RelAssociatesRepository repository, RootRepository thingRepository) {
        super(repository);
        this.thingRepository = thingRepository;
    }

    @Override
    protected XtdRelAssociates newEntityInstance() {
        return new XtdRelAssociates();
    }

    @Override
    protected void setEntityProperties(XtdRelAssociates entity, AssociationInput dto) {
        final String relatingId = dto.getRelatingThing();
        final List<String> relatedIds = dto.getRelatedThings();

        super.setEntityProperties(entity, dto);
        mapRelating(entity, relatingId);
        mapRelated(entity, relatedIds);
    }

    @Override
    protected void updateEntityProperties(XtdRelAssociates entity, AssociationUpdateInput dto) {
        final List<String> relatedIds = entity.getRelatedThings()
                .stream().map(Entity::getId)
                .collect(Collectors.toList());
        final String newRelatingId = dto.getRelatingThing();
        final List<String> newRelatedIds = dto.getRelatedThings();

        super.updateEntityProperties(entity, dto);
        mapRelating(entity, newRelatingId);
        entity.getRelatedThings().removeIf(thing -> !newRelatedIds.contains(thing.getId()));
        newRelatedIds.removeAll(relatedIds);
        mapRelated(entity, newRelatedIds);
    }

    private void mapRelating(XtdRelAssociates entity, String relatingId) {
        thingRepository
                .findById(relatingId)
                .ifPresentOrElse(
                        entity::setRelatingThing,
                        ServiceUtil.throwEntityNotFoundException(relatingId)
                );
    }

    private void mapRelated(XtdRelAssociates entity, List<String> relatedIds) {
        var relatedThings = thingRepository.findAllById(relatedIds);
        List<XtdRoot> target = new ArrayList<>();
        relatedThings.forEach(target::add);
        entity.getRelatedThings().addAll(target);
    }
}
