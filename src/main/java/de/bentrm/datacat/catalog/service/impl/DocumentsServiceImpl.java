package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.XtdExternalDocument;
import de.bentrm.datacat.catalog.domain.XtdRelDocuments;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import de.bentrm.datacat.catalog.repository.DocumentsRepository;
import de.bentrm.datacat.catalog.repository.ExternalDocumentRepository;
import de.bentrm.datacat.catalog.repository.RootRepository;
import de.bentrm.datacat.catalog.service.DocumentsService;
import de.bentrm.datacat.catalog.service.EntityMapper;
import de.bentrm.datacat.catalog.service.value.OneToManyRelationshipValue;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Service
@Validated
@Transactional(readOnly = true)
public class DocumentsServiceImpl extends AbstractServiceImpl<XtdRelDocuments> implements DocumentsService {

    private final EntityMapper entityMapper = EntityMapper.INSTANCE;
    private final ExternalDocumentRepository externalDocumentRepository;
    private final RootRepository rootRepository;

    public DocumentsServiceImpl(SessionFactory sessionFactory,
                                DocumentsRepository repository,
                                ExternalDocumentRepository externalDocumentRepository,
                                RootRepository rootRepository) {
        super(XtdRelDocuments.class, sessionFactory, repository);
        this.externalDocumentRepository = externalDocumentRepository;
        this.rootRepository = rootRepository;
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

        return getRepository().save(ref);
    }
}
