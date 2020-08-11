package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.domain.relationship.XtdRelDocuments;
import de.bentrm.datacat.graphql.dto.DocumentsInput;
import de.bentrm.datacat.graphql.dto.DocumentsUpdateInput;
import de.bentrm.datacat.repository.ExternalDocumentRepository;
import de.bentrm.datacat.repository.RelDocumentsRepository;
import de.bentrm.datacat.repository.RootRepository;
import de.bentrm.datacat.service.RelDocumentsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        final String relatingId = dto.getRelatingDocument();
        final List<String> relatedIds = dto.getRelatedThings();

        super.setEntityProperties(entity, dto);
        mapRelating(entity, relatingId);
        mapRelated(entity, relatedIds);
    }

    @Override
    protected void updateEntityProperties(XtdRelDocuments entity, DocumentsUpdateInput dto) {
        final List<String> relatedIds = entity.getRelatedThings()
                .stream().map(XtdRoot::getId)
                .collect(Collectors.toList());
        final String newRelatingId = dto.getRelatingDocument();
        final List<String> newRelatedIds = dto.getRelatedThings();

        super.updateEntityProperties(entity, dto);
        mapRelating(entity, newRelatingId);
        entity.getRelatedThings().removeIf(thing -> !newRelatedIds.contains(thing.getId()));
        newRelatedIds.removeAll(relatedIds);
        mapRelated(entity, newRelatedIds);
    }

    private void mapRelating(XtdRelDocuments entity, String relatingId) {
        externalDocumentRepository
                .findById(relatingId)
                .ifPresentOrElse(
                        entity::setRelatingDocument,
                        ServiceUtil.throwEntityNotFoundException(relatingId)
                );
    }

    private void mapRelated(XtdRelDocuments entity, List<String> relatedIds) {
        List<XtdRoot> target = new ArrayList<>();
        thingsRepository.findAllById(relatedIds).forEach(target::add);
        entity.getRelatedThings().addAll(target);
    }
}
