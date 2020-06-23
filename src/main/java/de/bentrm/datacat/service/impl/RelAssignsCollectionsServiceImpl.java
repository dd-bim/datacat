package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.XtdObject;
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
        super.setEntityProperties(entity, dto);

        XtdObject relating = objectRepository
                .findById(dto.getRelatingObject())
                .orElseThrow(() -> new IllegalArgumentException("No Object with id " + dto.getRelatingObject() + " found."));
        entity.setRelatingObject(relating);

        Specification spec = Specification.unspecified();
        spec.setIdIn(dto.getRelatedCollections());
        spec.setPageSize(1000);
        Page<XtdCollection> relatedCollections = collectionRepository.findAll(spec);
        entity.getRelatedCollections().addAll(relatedCollections.getContent());
    }

    @Override
    protected void updateEntityProperties(XtdRelAssignsCollections entity, AssignsCollectionsUpdateInput dto) {
        super.updateEntityProperties(entity, dto);

        if (!dto.getRelatingObject().equals(entity.getRelatingObject().getId())) {
            throw new IllegalArgumentException("Relating side of relationship can't be changed. Create a new relationship instead.'");
        }

        // remove things no longer in this relationship
        entity.getRelatedCollections().removeIf(thing -> !dto.getRelatedCollections().contains(thing.getId()));

        // add new things to this relationship
        Specification spec = Specification.unspecified();
        spec.setIdIn(dto.getRelatedCollections());
        spec.setPageSize(1000);
        Page<XtdCollection> relatedThings = collectionRepository.findAll(spec);
        entity.getRelatedCollections().addAll(relatedThings.getContent());
    }
}
