package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.XtdObject;
import de.bentrm.datacat.domain.relationship.XtdRelDocuments;
import de.bentrm.datacat.domain.relationship.XtdRelGroups;
import de.bentrm.datacat.domain.relationship.XtdRelationship;
import de.bentrm.datacat.dto.XtdRelGroupsInputDto;
import de.bentrm.datacat.repository.LanguageRepository;
import de.bentrm.datacat.repository.ObjectRepository;
import de.bentrm.datacat.repository.relationship.RelDocumentsRepository;
import de.bentrm.datacat.repository.relationship.RelGroupsRepository;
import de.bentrm.datacat.repository.relationship.RelationshipRepository;
import de.bentrm.datacat.service.XtdRelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class XtdRelationshipServiceImpl extends NamedEntityServiceImpl<XtdRelationship, RelationshipRepository> implements XtdRelationshipService {

    private final ObjectRepository objectRepository;
    private final RelDocumentsRepository relDocumentsRepository;
    private final RelGroupsRepository relGroupsRepository;

    @Autowired
    public XtdRelationshipServiceImpl(
            LanguageRepository languageRepository,
            RelationshipRepository repository,
            ObjectRepository objectRepository,
            RelDocumentsRepository relDocumentsRepository,
            RelGroupsRepository relGroupsRepository) {
        super(languageRepository, repository);
        this.objectRepository = objectRepository;
        this.relDocumentsRepository = relDocumentsRepository;
        this.relGroupsRepository = relGroupsRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<XtdRelationship> findAll(String label, int pageNumber, int pageSize) {
        Iterable<XtdRelGroups> queryResults = relGroupsRepository.findAll(3);
        long totalCount = relGroupsRepository.count();

        List<XtdRelationship> pageResults = new ArrayList<>();
        queryResults.forEach(pageResults::add);
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, "name");
        return new PageImpl<>(pageResults, pageRequest, totalCount);
    }

    @Override
    public XtdRelDocuments findRelDocumentsByUniqueId(String uniqueId) {
        return relDocumentsRepository.findByUniqueId(uniqueId);
    }

    @Override
    public Page<XtdRelDocuments> findAllRelDocuments(int pageNumber, int pageSize) {
        int skip = pageNumber * pageSize;
        Iterable<XtdRelDocuments> relationships = relDocumentsRepository.findAllOrderedByRelatingDocumentName(skip, pageSize);

        List<XtdRelDocuments> content = new ArrayList<>();
        relationships.forEach(content::add);

        return PageableExecutionUtils.getPage(content, PageRequest.of(pageNumber, pageSize), relDocumentsRepository::count);
    }

    @Override
    public Page<XtdRelDocuments> findRelDocumentsByRelatingDocument(String relatingDocumentUniqueId, int pageNumber, int pageSize) {
        int skip = pageNumber * pageSize;
        Iterable<XtdRelDocuments> relationships = relDocumentsRepository
                .findByRelatingDocumentOrderedByRelatingDocumentName(relatingDocumentUniqueId, skip, pageSize);

        List<XtdRelDocuments> content = new ArrayList<>();
        relationships.forEach(content::add);

        return PageableExecutionUtils.getPage(content, PageRequest.of(pageNumber, pageSize),
                () -> relDocumentsRepository.countByRelatingDocument(relatingDocumentUniqueId));
    }

    @Override
    public XtdRelGroups createRelGroups(XtdRelGroupsInputDto dto) {
        XtdRelGroups newRelGroups = new XtdRelGroups();
        mapInputToObject(newRelGroups, dto);

        XtdObject relatingObject = objectRepository.findByUniqueId(dto.getRelatingObjectUniqueId());
        if (relatingObject == null) {
            throw new IllegalArgumentException("No relating object provided.");
        }
        newRelGroups.setRelatingObject(relatingObject);

        dto.getRelatedObjectsUniqueIds().forEach(uniqueId -> {
            XtdObject relatedObject = objectRepository.findByUniqueId(uniqueId);
            if (relatedObject == null) {
                throw new IllegalArgumentException("No related object with ID " + uniqueId + " found.");
            }
            newRelGroups.getRelatedObjects().add(relatedObject);
        });

        return relGroupsRepository.save(newRelGroups);
    }

    @Override
    public Page<XtdRelGroups> findRelGroupsByRelatingObjectUniqueId(
            String relatingObjectUniqueId, int pageNumber, int pageSize) {
        int skip = pageNumber * pageSize;
        Iterable<XtdRelGroups> relationships = relGroupsRepository
                .findByRelatingObjectOrderedByRelatingObjectName(relatingObjectUniqueId, skip, pageSize);

        List<XtdRelGroups> content = new ArrayList<>();
        relationships.forEach(content::add);

        return PageableExecutionUtils.getPage(content, PageRequest.of(pageNumber, pageSize),
                () -> relGroupsRepository.countByRelatingObject(relatingObjectUniqueId));
    }

    @Override
    public Page<XtdRelGroups> findAllRelGroups(int pageNumber, int pageSize) {
        int skip = pageNumber * pageSize;
        Iterable<XtdRelGroups> relationships = relGroupsRepository.findAllOrderedByRelatingObjectName(skip, pageSize);

        List<XtdRelGroups> content = new ArrayList<>();
        relationships.forEach(content::add);

        return PageableExecutionUtils.getPage(content, PageRequest.of(pageNumber, pageSize), relGroupsRepository::count);
    }

    @Override
    public XtdRelGroups findRelGroupsByUniqueId(String uniqueId) {
        return relGroupsRepository.findByUniqueId(uniqueId, 3);
    }
}
