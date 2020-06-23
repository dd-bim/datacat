package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.domain.collection.XtdCollection;
import de.bentrm.datacat.domain.relationship.XtdRelCollects;
import de.bentrm.datacat.graphql.dto.CollectsInput;
import de.bentrm.datacat.graphql.dto.CollectsUpdateInput;
import de.bentrm.datacat.repository.CollectionRepository;
import de.bentrm.datacat.repository.RelCollectsRepository;
import de.bentrm.datacat.repository.RootRepository;
import de.bentrm.datacat.service.RelCollectsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

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
        super.setEntityProperties(entity, dto);

        XtdCollection relating = collectionRepository
                .findById(dto.getRelatingCollection())
                .orElseThrow(() -> new IllegalArgumentException("No Object with id " + dto.getRelatingCollection() + " found."));
        entity.setRelatingCollection(relating);

        Page<XtdRoot> relatedThings = thingRepository.findAllById(dto.getRelatedThings(), PageRequest.of(0, 1000));
        entity.getRelatedThings().addAll(relatedThings.getContent());
    }

    @Override
    protected void updateEntityProperties(XtdRelCollects entity, CollectsUpdateInput dto) {
        super.updateEntityProperties(entity, dto);

        if (!dto.getRelatingCollection().equals(entity.getRelatingCollection().getId())) {
            throw new IllegalArgumentException("Relating side of relationship can't be changed. Create a new relationship instead.'");
        }

        // remove things no longer in this relationship
        entity.getRelatedThings().removeIf(thing -> !dto.getRelatedThings().contains(thing.getId()));

        // add new things to this relationship
        Page<XtdRoot> relatedThings = thingRepository.findAllById(dto.getRelatedThings(), PageRequest.of(0, 1000));
        entity.getRelatedThings().addAll(relatedThings.getContent());
    }
}
