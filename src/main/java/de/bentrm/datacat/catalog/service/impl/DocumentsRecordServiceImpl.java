// package de.bentrm.datacat.catalog.service.impl;

// import de.bentrm.datacat.catalog.domain.CatalogRecordType;
// import de.bentrm.datacat.catalog.domain.XtdExternalDocument;
// import de.bentrm.datacat.catalog.domain.XtdRelDocuments;
// import de.bentrm.datacat.catalog.domain.XtdRoot;
// import de.bentrm.datacat.catalog.repository.DocumentsRepository;
// import de.bentrm.datacat.catalog.repository.ExternalDocumentRepository;
// import de.bentrm.datacat.catalog.repository.RootRepository;
// import de.bentrm.datacat.catalog.service.CatalogCleanupService;
// import de.bentrm.datacat.catalog.service.DocumentsRecordService;
// import org.neo4j.ogm.session.SessionFactory;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;
// import org.springframework.validation.annotation.Validated;

// import javax.validation.constraints.NotBlank;
// import javax.validation.constraints.NotEmpty;
// import javax.validation.constraints.NotNull;
// import java.util.List;
// import java.util.stream.Collectors;
// import java.util.stream.StreamSupport;

// @Service
// @Validated
// @Transactional(readOnly = true)
// public class DocumentsRecordServiceImpl
//         extends AbstractRelationshipRecordServiceImpl<XtdRelDocuments, DocumentsRepository>
//         implements DocumentsRecordService {

//     private final ExternalDocumentRepository externalDocumentRepository;
//     private final RootRepository rootRepository;

//     public DocumentsRecordServiceImpl(SessionFactory sessionFactory,
//                                       DocumentsRepository repository,
//                                       CatalogCleanupService cleanupService,
//                                       ExternalDocumentRepository externalDocumentRepository,
//                                       RootRepository rootRepository) {
//         super(XtdRelDocuments.class, sessionFactory, repository, cleanupService);
//         this.externalDocumentRepository = externalDocumentRepository;
//         this.rootRepository = rootRepository;
//     }

//     @Override
//     public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
//         return CatalogRecordType.Documents;
//     }

//     @Override
//     protected void setRelatingRecord(@NotNull XtdRelDocuments relationshipRecord, @NotBlank String relatingRecordId) {
//         final XtdExternalDocument relating = externalDocumentRepository
//                 .findById(relatingRecordId, 0)
//                 .orElseThrow();
//         relationshipRecord.setRelatingDocument(relating);
//     }

//     @Override
//     protected void setRelatedRecords(@NotNull XtdRelDocuments relationshipRecord, @NotEmpty List<@NotBlank String> relatedRecordIds) {
//         final Iterable<XtdRoot> things = rootRepository.findAllById(relatedRecordIds, 0);
//         final List<XtdRoot> related = StreamSupport
//                 .stream(things.spliterator(), false)
//                 .collect(Collectors.toList());
//         relationshipRecord.getRelatedThings().clear();
//         relationshipRecord.getRelatedThings().addAll(related);
//     }
// }
