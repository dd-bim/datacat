package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.XtdExternalDocument;
import de.bentrm.datacat.domain.XtdLanguage;
import de.bentrm.datacat.domain.XtdName;
import de.bentrm.datacat.domain.XtdSubject;
import de.bentrm.datacat.repository.ExternalDocumentRepository;
import de.bentrm.datacat.repository.LanguageRepository;
import de.bentrm.datacat.repository.relationship.RelDocumentsRepository;
import de.bentrm.datacat.service.XtdExternalDocumentService;
import de.bentrm.datacat.dto.XtdExternalDocumentInputDto;
import de.bentrm.datacat.dto.XtdNameInputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class XtdExternalDocumentServiceImpl extends NamedEntityServiceImpl<XtdExternalDocument, ExternalDocumentRepository> implements XtdExternalDocumentService {

    private final RelDocumentsRepository relDocumentsRepository;

    @Autowired
    public XtdExternalDocumentServiceImpl(
            LanguageRepository languageRepository,
            ExternalDocumentRepository repository,
            RelDocumentsRepository relDocumentsRepository
    ) {
        super(languageRepository, repository);
        this.relDocumentsRepository = relDocumentsRepository;
    }

    public XtdExternalDocument createExternalDocument(XtdExternalDocumentInputDto dto) {
        XtdExternalDocument newExternalDocument = new XtdExternalDocument();
        newExternalDocument.setUniqueId(dto.getUniqueId());
        for (int i = 0; i < dto.getNames().size(); i++) {
            XtdNameInputDto nameDto = dto.getNames().get(i);
            XtdName newName = new XtdName();
            XtdLanguage language = languageRepository.findByLanguageCode(nameDto.getLanguageCode());
            newName.setUniqueId(nameDto.getUniqueId());
            newName.setLanguageName(language);
            newName.setName(nameDto.getName());
            newName.setSortOrder(i);
            newExternalDocument.getNames().add(newName);
        }
        return repository.save(newExternalDocument);
    }

    @Override
    public XtdExternalDocument deleteExternalDocument(String uniqueId) {
        XtdExternalDocument document = repository.findByUniqueId(uniqueId);
        document.getDocuments().forEach(relDocumentsRepository::delete);
        repository.delete(document);
        return document;
    }

    @Override
    public Page<XtdExternalDocument> findAllExternalDocuments(int pageNumber, int pageSize) {
        int skip = pageNumber * pageSize;
        Iterable<XtdExternalDocument> documents = repository.findAllOrderedByName(skip, pageSize);

        List<XtdExternalDocument> pageContent = new ArrayList<>();
        documents.forEach(pageContent::add);

        return new PageImpl<>(pageContent, PageRequest.of(pageNumber, pageSize), repository.count());
    }

    @Override
    public Page<XtdExternalDocument> findExternalDocumentsByTerm(String term, int pageNumber, int pageSize) {
        int skip = pageNumber * pageSize;
        Iterable<XtdExternalDocument> documents = repository.findByTerm(term, pageNumber, pageSize);

        List<XtdExternalDocument> pageContent = new ArrayList<>();
        documents.forEach(pageContent::add);

        return new PageImpl<>(pageContent, PageRequest.of(pageNumber, pageSize), repository.countByTerm(term));
    }
}
