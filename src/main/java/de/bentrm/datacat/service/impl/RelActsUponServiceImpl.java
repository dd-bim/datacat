package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.Entity;
import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.domain.relationship.XtdRelActsUpon;
import de.bentrm.datacat.graphql.dto.AssociationInput;
import de.bentrm.datacat.graphql.dto.AssociationUpdateInput;
import de.bentrm.datacat.repository.RelActsUponRepository;
import de.bentrm.datacat.repository.RootRepository;
import de.bentrm.datacat.service.RelActsUponService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
@Transactional(readOnly = true)
public class RelActsUponServiceImpl
        extends CrudRootServiceImpl<XtdRelActsUpon, AssociationInput, AssociationUpdateInput, RelActsUponRepository>
        implements RelActsUponService {

    private final RootRepository thingRepository;

    public RelActsUponServiceImpl(RelActsUponRepository repository, RootRepository thingRepository) {
        super(repository);
        this.thingRepository = thingRepository;
    }

    @Override
    protected XtdRelActsUpon newEntityInstance() {
        return new XtdRelActsUpon();
    }

    @Override
    protected void setEntityProperties(XtdRelActsUpon entity, AssociationInput dto) {
        final String relatingThingId = dto.getRelatingThing();
        final List<String> relatedThingsIds = dto.getRelatedThings();

        super.setEntityProperties(entity, dto);
        mapRelatingThing(entity, relatingThingId);
        mapRelatedThings(entity, relatedThingsIds);
    }

    @Override
    protected void updateEntityProperties(XtdRelActsUpon entity, AssociationUpdateInput dto) {
        final List<String> preexistingIds = entity.getRelatedThings()
                .stream().map(Entity::getId)
                .collect(Collectors.toList());
        final String relatingThingId = dto.getRelatingThing();
        final List<String> submittedIds = dto.getRelatedThings();

        super.updateEntityProperties(entity, dto);
        mapRelatingThing(entity, relatingThingId);
        entity.getRelatedThings().removeIf(thing -> !submittedIds.contains(thing.getId()));
        submittedIds.removeAll(preexistingIds);
        mapRelatedThings(entity, submittedIds);
    }

    private void mapRelatingThing(XtdRelActsUpon entity, String relatingThingId) {
        thingRepository
                .findById(relatingThingId)
                .ifPresentOrElse(
                        entity::setRelatingThing,
                        ServiceUtil.throwEntityNotFoundException(relatingThingId)
                );
    }

    private void mapRelatedThings(XtdRelActsUpon entity, List<String> relatedThingsIds) {
        final Iterable<XtdRoot> relatedThings = thingRepository.findAllById(relatedThingsIds);
        List<XtdRoot> target = new ArrayList<>();
        relatedThings.forEach(target::add);
        entity.getRelatedThings().addAll(target);
    }
}
