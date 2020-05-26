package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.XtdExternalDocument;
import de.bentrm.datacat.domain.XtdObject;
import de.bentrm.datacat.domain.relationship.XtdRelDocuments;
import de.bentrm.datacat.graphql.dto.RelDocumentsInput;
import de.bentrm.datacat.graphql.dto.RelDocumentsUpdateInput;
import de.bentrm.datacat.repository.ExternalDocumentRepository;
import de.bentrm.datacat.repository.ObjectRepository;
import de.bentrm.datacat.repository.RelDocumentsRepository;
import de.bentrm.datacat.service.RelDocumentsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional(readOnly = true)
public class RelDocumentsServiceImpl
        extends CrudEntityServiceImpl<XtdRelDocuments, RelDocumentsInput, RelDocumentsUpdateInput, RelDocumentsRepository>
        implements RelDocumentsService {

    private final ExternalDocumentRepository externalDocumentRepository;
    private final ObjectRepository objectRepository;

    public RelDocumentsServiceImpl(RelDocumentsRepository repository, ExternalDocumentRepository externalDocumentRepository, ObjectRepository objectRepository) {
        super(repository);
        this.externalDocumentRepository = externalDocumentRepository;
        this.objectRepository = objectRepository;
    }

    @Override
    protected XtdRelDocuments newEntityInstance() {
        return new XtdRelDocuments();
    }

    @Override
    protected void setEntityProperties(XtdRelDocuments entity, RelDocumentsInput dto) {
        super.setEntityProperties(entity, dto);
        XtdExternalDocument relating = externalDocumentRepository
                .findById(dto.getRelatingDocument())
                .orElseThrow(() -> new IllegalArgumentException("No Object with id " + dto.getRelatingDocument() + " found."));
        entity.setRelatingDocument(relating);

        Page<XtdObject> relatedObjects = objectRepository.findAllById(dto.getRelatedObjects(), PageRequest.of(0, 1000));
        entity.getRelatedObjects().addAll(relatedObjects.getContent());
    }

    @Override
    protected void updateEntityProperties(XtdRelDocuments entity, RelDocumentsUpdateInput dto) {
        super.updateEntityProperties(entity, dto);

        if (!dto.getRelatingDocument().equals(entity.getRelatingDocument().getId())) {
            throw new IllegalArgumentException("Relating side of relationship can't be changed. Create a new relationship instead.'");
        }

        // remove things no longer in this relationship
        entity.getRelatedObjects().removeIf(thing -> !dto.getRelatedObjects().contains(thing.getId()));

        // add new things to this relationship
        Page<XtdObject> relatedObjects = objectRepository.findAllById(dto.getRelatedObjects(), PageRequest.of(0, 1000));
        entity.getRelatedObjects().addAll(relatedObjects.getContent());
    }
}
