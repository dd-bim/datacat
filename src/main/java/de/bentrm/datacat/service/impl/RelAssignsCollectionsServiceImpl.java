package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.Entity;
import de.bentrm.datacat.domain.collection.XtdCollection;
import de.bentrm.datacat.domain.relationship.XtdRelAssignsCollections;
import de.bentrm.datacat.graphql.dto.AssignsCollectionsInput;
import de.bentrm.datacat.graphql.dto.AssignsCollectionsUpdateInput;
import de.bentrm.datacat.repository.CollectionRepository;
import de.bentrm.datacat.repository.ObjectRepository;
import de.bentrm.datacat.repository.RelAssignsCollectionsRepository;
import de.bentrm.datacat.service.RelAssignsCollectionsService;
import de.bentrm.datacat.service.Specification;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
@Transactional(readOnly = true)
public class RelAssignsCollectionsServiceImpl
        extends CrudRootServiceImpl<XtdRelAssignsCollections, AssignsCollectionsInput, AssignsCollectionsUpdateInput, RelAssignsCollectionsRepository>
        implements RelAssignsCollectionsService {

    private final ObjectRepository objectRepository;
    private final CollectionRepository collectionRepository;

    public RelAssignsCollectionsServiceImpl(RelAssignsCollectionsRepository repository, ObjectRepository objectRepository, CollectionRepository collectionRepository) {
        super(repository);
        this.objectRepository = objectRepository;
        this.collectionRepository = collectionRepository;
    }

    @Override
    protected XtdRelAssignsCollections newEntityInstance() {
        return new XtdRelAssignsCollections();
    }

    @Override
    protected void setEntityProperties(XtdRelAssignsCollections entity, AssignsCollectionsInput dto) {
        final String relatingId = dto.getRelatingObject();
        final List<String> relatedIds = dto.getRelatedCollections();

        super.setEntityProperties(entity, dto);
        mapRelating(entity, relatingId);
        mapRelated(entity, relatedIds);
    }

    @Override
    protected void updateEntityProperties(XtdRelAssignsCollections entity, AssignsCollectionsUpdateInput dto) {
        final List<String> relatedIds = entity.getRelatedCollections()
                .stream().map(Entity::getId)
                .collect(Collectors.toList());
        final String newRelatingId = dto.getRelatingObject();
        final List<String> newRelatedIds = dto.getRelatedCollections();

        super.updateEntityProperties(entity, dto);
        mapRelating(entity, newRelatingId);
        entity.getRelatedCollections().removeIf(thing -> !newRelatedIds.contains(thing.getId()));
        newRelatedIds.removeAll(relatedIds);
        mapRelated(entity, newRelatedIds);
    }

    private void mapRelating(XtdRelAssignsCollections entity, String relatingId) {
        objectRepository
                .findById(relatingId)
                .ifPresentOrElse(
                        entity::setRelatingObject,
                        ServiceUtil.throwEntityNotFoundException(relatingId)
                );
    }

    private void mapRelated(XtdRelAssignsCollections entity, List<String> relatedIds) {
        final Specification spec = Specification
                .unspecified()
                .setIdIn(relatedIds);
        final Page<XtdCollection> relatedThings = collectionRepository.findAll(spec);
        entity.getRelatedCollections().addAll(relatedThings.getContent());
    }
}
