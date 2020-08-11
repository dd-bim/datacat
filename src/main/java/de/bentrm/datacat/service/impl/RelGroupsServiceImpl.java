package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.Entity;
import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.domain.relationship.XtdRelGroups;
import de.bentrm.datacat.graphql.dto.AssociationInput;
import de.bentrm.datacat.graphql.dto.AssociationUpdateInput;
import de.bentrm.datacat.repository.RelGroupsRepository;
import de.bentrm.datacat.repository.RootRepository;
import de.bentrm.datacat.service.RelGroupsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
@Transactional(readOnly = true)
public class RelGroupsServiceImpl
        extends CrudRootServiceImpl<XtdRelGroups, AssociationInput, AssociationUpdateInput, RelGroupsRepository>
        implements RelGroupsService {

    private final RootRepository thingRepository;

    public RelGroupsServiceImpl(RelGroupsRepository repository, RootRepository thingRepository) {
        super(repository);
        this.thingRepository = thingRepository;
    }

    @Override
    protected XtdRelGroups newEntityInstance() {
        return new XtdRelGroups();
    }

    @Override
    protected void setEntityProperties(XtdRelGroups entity, AssociationInput dto) {
        final String relatingId = dto.getRelatingThing();
        final List<String> relatedIds = dto.getRelatedThings();

        super.setEntityProperties(entity, dto);
        mapRelating(entity, relatingId);
        mapRelated(entity, relatedIds);
    }

    @Override
    protected void updateEntityProperties(XtdRelGroups entity, AssociationUpdateInput dto) {
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

    private void mapRelating(XtdRelGroups entity, String relatingId) {
        thingRepository
                .findById(relatingId)
                .ifPresentOrElse(
                        entity::setRelatingThing,
                        ServiceUtil.throwEntityNotFoundException(relatingId)
                );
    }

    private void mapRelated(XtdRelGroups entity, List<String> relatedIds) {
        List<XtdRoot> target = new ArrayList<>();
        thingRepository.findAllById(relatedIds).forEach(target::add);
        entity.getRelatedThings().addAll(target);
    }
}
