package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.XtdName;
import de.bentrm.datacat.domain.relationship.XtdRelGroups;
import de.bentrm.datacat.dto.RootInputDto;
import de.bentrm.datacat.repository.LanguageRepository;
import de.bentrm.datacat.repository.ObjectRepository;
import de.bentrm.datacat.repository.relationship.RelGroupsRepository;
import de.bentrm.datacat.service.RelGroupsService;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class RelGroupsServiceImpl implements RelGroupsService {

    Logger logger = LoggerFactory.getLogger(RelGroupsServiceImpl.class);

    @Autowired
    private Validator validator;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private ObjectRepository objectRepository;

    @Autowired
    private RelGroupsRepository relGroupsRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<XtdRelGroups> findById(String id) {
        return relGroupsRepository.findById(id, 2);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<XtdRelGroups> findAll(Pageable pageable) {
        Iterable<XtdRelGroups> nodes = relGroupsRepository.findAllOrderedByRelatingObjectName(pageable.getOffset(), pageable.getPageSize());
        List<XtdRelGroups> resultList = new ArrayList<>();
        nodes.forEach(resultList::add);
        return PageableExecutionUtils.getPage(resultList, pageable, relGroupsRepository::count);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<XtdRelGroups> findByTerm(String match, Pageable pageable) {
        throw new NotImplementedException("Missing repository method.");
    }

    @Override
    public XtdRelGroups create(String relatingObjectId, Set<String> relatedObjectsIds, RootInputDto dto) {
        Set<ConstraintViolation<RootInputDto>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<RootInputDto> violation : violations) {
                logger.error(violation.getMessage());
            }
            throw new ConstraintViolationException(violations);
        }

        XtdRelGroups newGroup = new XtdRelGroups();
        for (int i = 0; i < dto.getNames().size(); i++) {
            var nameDto = dto.getNames().get(i);
            XtdName newName = new XtdName();
            newName.setId(nameDto.getId());
            languageRepository.findById(nameDto.getLanguageCode()).ifPresentOrElse(
                    newName::setLanguageName,
                    () -> {
                        throw new IllegalArgumentException("Unsupported language code: " + nameDto.getLanguageCode());
                    }
            );
            newName.setName(nameDto.getValue());
            newName.setSortOrder(i);
            newGroup.getNames().add(newName);
        }

        objectRepository
                .findById(relatingObjectId)
                .ifPresentOrElse(newGroup::setRelatingObject, () -> {
                    throw new IllegalArgumentException("Relating object " + relatingObjectId + " not found.");
                });

        relatedObjectsIds.forEach(id -> {
            objectRepository.findById(id).ifPresentOrElse(
                    obj -> newGroup.getRelatedObjects().add(obj),
                    () -> {
                        throw new IllegalArgumentException("Related object with id " + id + " not found.");
                    }
            );
        });

        return relGroupsRepository.save(newGroup);
    }

    @Override
    public Optional<XtdRelGroups> delete(String id) {
        return Optional.empty();
    }

    @Override
    public XtdRelGroups addRelatedObjects(String id, List<String> relatedObjectsIds) {
        Optional<XtdRelGroups> result = relGroupsRepository.findById(id, 2);
        XtdRelGroups groups = result.orElseThrow(() -> new IllegalArgumentException("No relation found with id " + id));

        relatedObjectsIds.forEach(relatedObjectId -> objectRepository
                .findById(relatedObjectId)
                .ifPresentOrElse(
                        obj -> groups.getRelatedObjects().add(obj),
                        () -> {
                            throw new IllegalArgumentException("No object with id " + relatedObjectId + " found.");
                        }
                )
        );

        return relGroupsRepository.save(groups);
    }

    @Override
    public XtdRelGroups removeRelatedObjects(String id, List<String> relatedObjectsIds) {
        Optional<XtdRelGroups> result = relGroupsRepository.findById(id);
        XtdRelGroups groups = result.orElseThrow(() -> new IllegalArgumentException("No relation found with id " + id));
        groups.getRelatedObjects().removeIf(x -> relatedObjectsIds.contains(x.getId()));
        return relGroupsRepository.save(groups);
    }

    @Override
    public Page<XtdRelGroups> findByRelatingObjectId(String id, Pageable pageable) {
        Iterable<XtdRelGroups> nodes = relGroupsRepository.findByRelatingObjectOrderedByRelatingObjectName(id, pageable.getOffset(), pageable.getPageSize());
        List<XtdRelGroups> resultList = new ArrayList<>();
        nodes.forEach(resultList::add);
        return PageableExecutionUtils.getPage(resultList, pageable, () -> relGroupsRepository.countByRelatingObject(id));
    }

    @Override
    public Page<XtdRelGroups> findByRelatedObjectId(String id, Pageable pageable) {
        Iterable<XtdRelGroups> nodes = relGroupsRepository.findByRelatedObject(id, pageable.getOffset(), pageable.getPageSize());
        List<XtdRelGroups> resultList = new ArrayList<>();
        nodes.forEach(resultList::add);
        return PageableExecutionUtils.getPage(resultList, pageable, () -> relGroupsRepository.countByRelatedObject(id));
    }
}
