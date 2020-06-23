package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.XtdExternalDocument;
import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.domain.relationship.XtdRelDocuments;
import de.bentrm.datacat.graphql.dto.DocumentsInput;
import de.bentrm.datacat.graphql.dto.DocumentsUpdateInput;
import de.bentrm.datacat.repository.ExternalDocumentRepository;
import de.bentrm.datacat.repository.RelDocumentsRepository;
import de.bentrm.datacat.repository.RootRepository;
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
        extends CrudRootServiceImpl<XtdRelDocuments, DocumentsInput, DocumentsUpdateInput, RelDocumentsRepository>
        implements RelDocumentsService {

    private final ExternalDocumentRepository externalDocumentRepository;
    private final RootRepository thingsRepository;

    public RelDocumentsServiceImpl(RelDocumentsRepository repository, ExternalDocumentRepository externalDocumentRepository, RootRepository thingsRepository) {
        super(repository);
        this.externalDocumentRepository = externalDocumentRepository;
        this.thingsRepository = thingsRepository;
    }

    @Override
    protected XtdRelDocuments newEntityInstance() {
        return new XtdRelDocuments();
    }

    @Override
    protected void setEntityProperties(XtdRelDocuments entity, DocumentsInput dto) {
        super.setEntityProperties(entity, dto);
        XtdExternalDocument relating = externalDocumentRepository
                .findById(dto.getRelatingDocument())
                .orElseThrow(() -> new IllegalArgumentException("No Object with id " + dto.getRelatingDocument() + " found."));
        entity.setRelatingDocument(relating);

        Page<XtdRoot> relatedThings = thingsRepository.findAllById(dto.getRelatedThings(), PageRequest.of(0, 1000));
        entity.getRelatedThings().addAll(relatedThings.getContent());
    }

    @Override
    protected void updateEntityProperties(XtdRelDocuments entity, DocumentsUpdateInput dto) {
        super.updateEntityProperties(entity, dto);

        if (!dto.getRelatingDocument().equals(entity.getRelatingDocument().getId())) {
            throw new IllegalArgumentException("Relating side of relationship can't be changed. Create a new relationship instead.'");
        }

        // remove things no longer in this relationship
        entity.getRelatedThings().removeIf(thing -> !dto.getRelatedThings().contains(thing.getId()));

        // add new things to this relationship
        Page<XtdRoot> relatedThings = thingsRepository.findAllById(dto.getRelatedThings(), PageRequest.of(0, 1000));
        entity.getRelatedThings().addAll(relatedThings.getContent());
    }
}
