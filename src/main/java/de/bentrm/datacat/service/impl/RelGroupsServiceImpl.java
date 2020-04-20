package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.domain.relationship.XtdRelGroups;
import de.bentrm.datacat.graphql.dto.AssociationInput;
import de.bentrm.datacat.graphql.dto.AssociationUpdateInput;
import de.bentrm.datacat.repository.RelGroupsRepository;
import de.bentrm.datacat.repository.RootRepository;
import de.bentrm.datacat.service.RelGroupsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Service
@Validated
@Transactional(readOnly = true)
public class RelGroupsServiceImpl
        extends CrudRootServiceImpl<XtdRelGroups, AssociationInput, AssociationUpdateInput, RelGroupsRepository>
        implements RelGroupsService {

    Logger logger = LoggerFactory.getLogger(RelGroupsServiceImpl.class);

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
        super.setEntityProperties(entity, dto);

        XtdRoot relating = thingRepository
                .findById(dto.getRelatingThing())
                .orElseThrow(() -> new IllegalArgumentException("No Object with id " + dto.getRelatingThing() + " found."));
        entity.setRelatingThing(relating);

        Page<XtdRoot> relatedThings = thingRepository.findAllById(dto.getRelatedThings(), PageRequest.of(0, 1000));
        entity.getRelatedThings().addAll(relatedThings.getContent());
    }

    @Override
    protected void updateEntityProperties(XtdRelGroups entity, AssociationUpdateInput dto) {
        super.updateEntityProperties(entity, dto);

        if (!dto.getRelatingThing().equals(entity.getRelatingThing().getId())) {
            throw new IllegalArgumentException("Relating side of relationship can't be changed. Create a new relationship instead.'");
        }

        // remove things no longer in this relationship
        entity.getRelatedThings().removeIf(thing -> !dto.getRelatedThings().contains(thing.getId()));

        // add new things to this relationship
        Page<XtdRoot> relatedThings = thingRepository.findAllById(dto.getRelatedThings(), PageRequest.of(0, 1000));
        entity.getRelatedThings().addAll(relatedThings.getContent());
    }

    @Transactional
    public XtdRelGroups addRelatedObjects(String id, List<String> relatedObjectsIds) {
        XtdRelGroups relation = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No relation with id " + id + " found."));

        for (String relatedObjectId : relatedObjectsIds) {
            XtdRoot relatedObject = thingRepository
                    .findById(relatedObjectId)
                    .orElseThrow(() -> new IllegalArgumentException("No relatable object with id " + relatedObjectId + " found."));

            // TODO: Throw if related thing is already in persistent set
            relation.getRelatedThings().add(relatedObject);
        }

        return repository.save(relation);
    }

    @Transactional
    public XtdRelGroups removeRelatedObjects(String id, List<String> relatedObjectsIds) {
        XtdRelGroups relation = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No relation with id " + id + " found."));

        for (String relatedObjectId : relatedObjectsIds) {
            XtdRoot relatedThing = thingRepository
                    .findById(relatedObjectId)
                    .orElseThrow(() -> new IllegalArgumentException("No relatable object with id " + relatedObjectId + " found."));

            if (!relation.getRelatedThings().remove(relatedThing)) {
                throw new IllegalArgumentException("Object with id " + relatedObjectId + " is not related in relation " + id);
            }
        }

        if (relation.getRelatedThings().isEmpty()) {
            throw new IllegalArgumentException("Relation may not be empty");
        }

        return repository.save(relation);
    }

    @Override
    public Page<XtdRelGroups> findByRelatingThingId(@NotBlank String relatingObjectId, Pageable pageable) {
        return repository.findAllGroupedBy(relatingObjectId, pageable);
    }

    @Override
    public @NotNull Page<XtdRelGroups> findByRelatedThingId(@NotBlank String relatedObjectId, Pageable pageable) {
        return repository.findAllGrouping(relatedObjectId, pageable);
    }
}
