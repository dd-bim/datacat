package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.specification.QuerySpecification;
import de.bentrm.datacat.catalog.domain.XtdExternalDocument;
import de.bentrm.datacat.catalog.domain.XtdRelDocuments;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import de.bentrm.datacat.catalog.repository.ExternalDocumentRepository;
import de.bentrm.datacat.catalog.repository.RelDocumentsRepository;
import de.bentrm.datacat.catalog.repository.RootRepository;
import de.bentrm.datacat.catalog.service.DocumentsService;
import de.bentrm.datacat.catalog.service.EntityMapper;
import de.bentrm.datacat.catalog.service.value.OneToManyRelationshipValue;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Validated
@Transactional(readOnly = true)
public class DocumentsServiceImpl implements DocumentsService {

    private final EntityMapper entityMapper = EntityMapper.INSTANCE;
    private final RelDocumentsRepository documentsRepository;
    private final ExternalDocumentRepository externalDocumentRepository;
    private final RootRepository rootRepository;

    private final QueryServiceDelegate<XtdRelDocuments> queryDelegate;

    public DocumentsServiceImpl(RelDocumentsRepository repository,
                                ExternalDocumentRepository externalDocumentRepository, RootRepository rootRepository) {
        this.documentsRepository = repository;
        this.externalDocumentRepository = externalDocumentRepository;
        this.rootRepository = rootRepository;
        this.queryDelegate = new QueryServiceDelegate<>(repository);
    }

    @Transactional
    @Override
    public @NotNull XtdRelDocuments create(OneToManyRelationshipValue value) {
        final XtdRelDocuments ref = new XtdRelDocuments();

        entityMapper.setProperties(value, ref);

        final XtdExternalDocument relating = externalDocumentRepository.findById(value.getFrom()).orElseThrow();
        ref.setRelatingDocument(relating);

        final Iterable<XtdRoot> things = rootRepository.findAllById(value.getTo());
        final List<XtdRoot> related = new ArrayList<>();
        things.forEach(related::add);
        if (related.isEmpty()) {
            throw new IllegalArgumentException("A relationship must have at least one related member.");
        }
        ref.getRelatedThings().addAll(related);

        return documentsRepository.save(ref);
    }

    @Override
    public @NotNull Optional<XtdRelDocuments> findById(@NotNull String id) {
        return queryDelegate.findById(id);
    }

    @Override
    public @NotNull List<XtdRelDocuments> findAllByIds(@NotNull List<String> ids) {
        return queryDelegate.findAllByIds(ids);
    }

    @Override
    public @NotNull Page<XtdRelDocuments> findAll(@NotNull QuerySpecification specification) {
        return queryDelegate.findAll(specification);
    }

    @Override
    public @NotNull long count(@NotNull QuerySpecification specification) {
        return queryDelegate.count(specification);
    }
}
