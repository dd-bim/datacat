package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.Entity;
import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.domain.relationship.XtdRelSpecializes;
import de.bentrm.datacat.graphql.dto.AssociationInput;
import de.bentrm.datacat.graphql.dto.AssociationUpdateInput;
import de.bentrm.datacat.repository.RelSpecializesRepository;
import de.bentrm.datacat.repository.RootRepository;
import de.bentrm.datacat.service.RelSpecializesService;
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
public class RelSpecializesServiceImpl
        extends CrudRootServiceImpl<XtdRelSpecializes, AssociationInput, AssociationUpdateInput, RelSpecializesRepository>
        implements RelSpecializesService {

    private final RootRepository thingRepository;

    public RelSpecializesServiceImpl(RelSpecializesRepository repository, RootRepository thingRepository) {
        super(repository);
        this.thingRepository = thingRepository;
    }

    @Override
    protected XtdRelSpecializes newEntityInstance() {
        return new XtdRelSpecializes();
    }

    @Override
    protected void setEntityProperties(XtdRelSpecializes entity, AssociationInput dto) {
        final String relatingId = dto.getRelatingThing();
        final List<String> relatedIds = dto.getRelatedThings();

        super.setEntityProperties(entity, dto);
        mapRelating(entity, relatingId);
        mapRelated(entity, relatedIds);
    }

    @Override
    protected void updateEntityProperties(XtdRelSpecializes entity, AssociationUpdateInput dto) {
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
    public Page<XtdRelSpecializes> findByRelatingThingId(@NotBlank String relatingObjectId, Pageable pageable) {
        return repository.findAllSpecializedBy(relatingObjectId, pageable);
    }

    @Override
    public @NotNull Page<XtdRelSpecializes> findByRelatedThingId(@NotBlank String relatedObjectId, Pageable pageable) {
        return repository.findAllSpecializing(relatedObjectId, pageable);
    }

    private void mapRelating(XtdRelSpecializes entity, String relatingId) {
        thingRepository
                .findById(relatingId)
                .ifPresentOrElse(
                        entity::setRelatingThing,
                        ServiceUtil.throwEntityNotFoundException(relatingId)
                );
    }

    private void mapRelated(XtdRelSpecializes entity, List<String> relatedIds) {
        final Specification spec = Specification
                .unspecified()
                .setIdIn(relatedIds);
        final Page<XtdRoot> relatedThings = thingRepository.findAll(spec);
        entity.getRelatedThings().addAll(relatedThings.getContent());
    }
}
