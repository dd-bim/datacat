package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.Entity;
import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.domain.relationship.XtdRelAssociates;
import de.bentrm.datacat.graphql.dto.AssociationInput;
import de.bentrm.datacat.graphql.dto.AssociationUpdateInput;
import de.bentrm.datacat.repository.RelAssociatesRepository;
import de.bentrm.datacat.repository.RootRepository;
import de.bentrm.datacat.service.RelAssociatesService;
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

    @Override
    public Page<XtdRelAssociates> findByRelatingThingId(@NotBlank String relatingObjectId, Pageable pageable) {
        return repository.findAllAssociatedBy(relatingObjectId, pageable);
    }

    @Override
    public @NotNull Page<XtdRelAssociates> findByRelatedThingId(@NotBlank String relatedObjectId, Pageable pageable) {
        return repository.findAllAssociating(relatedObjectId, pageable);
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
        final Specification spec = Specification
                .unspecified()
                .setIdIn(relatedIds);
        final Page<XtdRoot> relatedThings = thingRepository.findAll(spec);
        entity.getRelatedThings().addAll(relatedThings.getContent());
    }
}
