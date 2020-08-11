package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.Entity;
import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.domain.relationship.XtdRelCollects;
import de.bentrm.datacat.graphql.dto.CollectsInput;
import de.bentrm.datacat.graphql.dto.CollectsUpdateInput;
import de.bentrm.datacat.repository.CollectionRepository;
import de.bentrm.datacat.repository.RelCollectsRepository;
import de.bentrm.datacat.repository.RootRepository;
import de.bentrm.datacat.service.RelCollectsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
@Transactional(readOnly = true)
public class RelCollectsServiceImpl
        extends CrudRootServiceImpl<XtdRelCollects, CollectsInput, CollectsUpdateInput, RelCollectsRepository>
        implements RelCollectsService {

    private final CollectionRepository collectionRepository;
    private final RootRepository thingRepository;

    public RelCollectsServiceImpl(RelCollectsRepository repository, CollectionRepository collectionRepository, RootRepository thingRepository) {
        super(repository);
        this.collectionRepository = collectionRepository;
        this.thingRepository = thingRepository;
    }

    @Override
    protected XtdRelCollects newEntityInstance() {
        return new XtdRelCollects();
    }

    @Override
    protected void setEntityProperties(XtdRelCollects entity, CollectsInput dto) {
        final String relatingId = dto.getRelatingCollection();
        final List<String> relatedIds = dto.getRelatedThings();

        super.setEntityProperties(entity, dto);
        mapRelating(entity, relatingId);
        mapRelated(entity, relatedIds);
    }

    @Override
    protected void updateEntityProperties(XtdRelCollects entity, CollectsUpdateInput dto) {
        final List<String> relatedIds = entity.getRelatedThings()
                .stream().map(Entity::getId)
                .collect(Collectors.toList());
        final String newRelatingId = dto.getRelatingCollection();
        final List<String> newRelatedIds = dto.getRelatedThings();

        super.updateEntityProperties(entity, dto);
        mapRelating(entity, newRelatingId);
        entity.getRelatedThings().removeIf(thing -> !newRelatedIds.contains(thing.getId()));
        newRelatedIds.removeAll(relatedIds);
        mapRelated(entity, newRelatedIds);
    }

    private void mapRelating(XtdRelCollects entity, String relatingCollectionId) {
        collectionRepository
                .findById(relatingCollectionId)
                .ifPresentOrElse(
                        entity::setRelatingCollection,
                        ServiceUtil.throwEntityNotFoundException(relatingCollectionId)
                );
    }

    private void mapRelated(XtdRelCollects entity, List<String> relatedThingsIds) {
        var relatedThings = thingRepository.findAllById(relatedThingsIds);
        List<XtdRoot> target = new ArrayList<>();
        relatedThings.forEach(target::add);
        entity.getRelatedThings().addAll(target);
    }
}
