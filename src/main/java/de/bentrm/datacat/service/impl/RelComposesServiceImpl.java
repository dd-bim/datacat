package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.Entity;
import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.domain.relationship.XtdRelComposes;
import de.bentrm.datacat.graphql.dto.AssociationInput;
import de.bentrm.datacat.graphql.dto.AssociationUpdateInput;
import de.bentrm.datacat.repository.RelComposesRepository;
import de.bentrm.datacat.repository.RootRepository;
import de.bentrm.datacat.service.RelComposesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
@Transactional(readOnly = true)
public class RelComposesServiceImpl
        extends CrudRootServiceImpl<XtdRelComposes, AssociationInput, AssociationUpdateInput, RelComposesRepository>
        implements RelComposesService {

    private final RootRepository thingRepository;

    public RelComposesServiceImpl(RelComposesRepository repository, RootRepository thingRepository) {
        super(repository);
        this.thingRepository = thingRepository;
    }

    @Override
    protected XtdRelComposes newEntityInstance() {
        return new XtdRelComposes();
    }

    @Override
    protected void setEntityProperties(XtdRelComposes entity, AssociationInput dto) {
        final String relatingId = dto.getRelatingThing();
        final List<String> relatedIds = dto.getRelatedThings();

        super.setEntityProperties(entity, dto);
        mapRelating(entity, relatingId);
        mapRelated(entity, relatedIds);
    }

    @Override
    protected void updateEntityProperties(XtdRelComposes entity, AssociationUpdateInput dto) {
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

    private void mapRelating(XtdRelComposes entity, String relatingCollectionId) {
        thingRepository
                .findById(relatingCollectionId)
                .ifPresentOrElse(
                        entity::setRelatingThing,
                        ServiceUtil.throwEntityNotFoundException(relatingCollectionId)
                );
    }

    private void mapRelated(XtdRelComposes entity, List<String> relatedThingsIds) {
        List<XtdRoot> target = new ArrayList<>();
        thingRepository.findAllById(relatedThingsIds).forEach(target::add);
        entity.getRelatedThings().addAll(target);
    }
}
