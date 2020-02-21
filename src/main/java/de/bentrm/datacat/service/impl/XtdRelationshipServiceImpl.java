package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.XtdObject;
import de.bentrm.datacat.domain.relationship.XtdRelGroups;
import de.bentrm.datacat.domain.relationship.XtdRelationship;
import de.bentrm.datacat.dto.XtdRelGroupsInputDto;
import de.bentrm.datacat.repository.LanguageRepository;
import de.bentrm.datacat.repository.ObjectRepository;
import de.bentrm.datacat.repository.relationship.RelGroupsRepository;
import de.bentrm.datacat.repository.relationship.RelationshipRepository;
import de.bentrm.datacat.service.XtdRelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class XtdRelationshipServiceImpl extends NamedEntityServiceImpl<XtdRelationship, RelationshipRepository> implements XtdRelationshipService {

    private final ObjectRepository objectRepository;
    private final RelGroupsRepository relGroupsRepository;

    @Autowired
    public XtdRelationshipServiceImpl(LanguageRepository languageRepository, RelationshipRepository repository, ObjectRepository objectRepository, RelGroupsRepository relGroupsRepository) {
        super(languageRepository, repository);
        this.objectRepository = objectRepository;
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
    public Iterable<XtdRelGroups> findRelGroups() {
        return relGroupsRepository.findAll(3);
    }

    @Override
    public XtdRelGroups findRelGroupsByUniqueId(String uniqueId) {
        return relGroupsRepository.findByUniqueId(uniqueId, 3);
    }

    @Override
    public List<XtdRelGroups> findAsscociationsByRelatingObjectUniqueId(String uniqueId) {
        System.out.println("Searching: " + uniqueId);
        return relGroupsRepository.findAllByRelatingObjectUniqueId(uniqueId, 3);
    }
}
