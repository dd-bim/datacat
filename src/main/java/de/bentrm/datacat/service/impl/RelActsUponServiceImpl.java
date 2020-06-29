package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.Entity;
import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.domain.relationship.XtdRelActsUpon;
import de.bentrm.datacat.graphql.dto.AssociationInput;
import de.bentrm.datacat.graphql.dto.AssociationUpdateInput;
import de.bentrm.datacat.repository.RelActsUponRepository;
import de.bentrm.datacat.repository.RootRepository;
import de.bentrm.datacat.service.RelActsUponService;
import de.bentrm.datacat.service.Specification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
@Transactional(readOnly = true)
public class RelActsUponServiceImpl
        extends CrudRootServiceImpl<XtdRelActsUpon, AssociationInput, AssociationUpdateInput, RelActsUponRepository>
        implements RelActsUponService {

    Logger logger = LoggerFactory.getLogger(RelActsUponServiceImpl.class);

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

    @Override
    public Page<XtdRelActsUpon> findByRelatingThingId(@NotBlank String relatingObjectId, Pageable pageable) {
        return repository.findAllActedUponBy(relatingObjectId, pageable);
    }

    @Override
    public @NotNull Page<XtdRelActsUpon> findByRelatedThingId(@NotBlank String relatedObjectId, Pageable pageable) {
        return repository.findAllActingUpon(relatedObjectId, pageable);
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
        final Specification spec = Specification
                .unspecified()
                .setIdIn(relatedThingsIds);
        final Page<XtdRoot> relatedThings = thingRepository.findAll(spec);
        entity.getRelatedThings().addAll(relatedThings.getContent());
    }
}
