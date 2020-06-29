package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.Entity;
import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.domain.relationship.XtdRelComposes;
import de.bentrm.datacat.graphql.dto.AssociationInput;
import de.bentrm.datacat.graphql.dto.AssociationUpdateInput;
import de.bentrm.datacat.repository.RelComposesRepository;
import de.bentrm.datacat.repository.RootRepository;
import de.bentrm.datacat.service.RelComposesService;
import de.bentrm.datacat.service.Specification;
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

    @Override
    public Page<XtdRelComposes> findByRelatingThingId(@NotBlank String relatingObjectId, Pageable pageable) {
        return repository.findAllComposedBy(relatingObjectId, pageable);
    }

    @Override
    public @NotNull Page<XtdRelComposes> findByRelatedThingId(@NotBlank String relatedObjectId, Pageable pageable) {
        return repository.findAllComposing(relatedObjectId, pageable);
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
        final Specification spec = Specification
                .unspecified()
                .setIdIn(relatedThingsIds);
        final Page<XtdRoot> relatedThings = thingRepository.findAll(spec);
        entity.getRelatedThings().addAll(relatedThings.getContent());
    }
}
