package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.XtdDescription;
import de.bentrm.datacat.domain.XtdName;
import de.bentrm.datacat.domain.XtdSubject;
import de.bentrm.datacat.graphql.dto.RootInput;
import de.bentrm.datacat.graphql.dto.RootUpdateInput;
import de.bentrm.datacat.graphql.dto.TextInput;
import de.bentrm.datacat.repository.SubjectRepository;
import de.bentrm.datacat.service.SubjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Validated
@Transactional(readOnly = true)
public class SubjectServiceImpl implements SubjectService {

    Logger logger = LoggerFactory.getLogger(SubjectServiceImpl.class);

    @Autowired
    private SubjectRepository subjectRepository;

    @Transactional
    @Override
    public XtdSubject create(RootInput dto) {
        XtdSubject newSubject = toEntity(dto);
        return subjectRepository.save(newSubject);
    }

    @Transactional
    @Override
    public XtdSubject update(@Valid RootUpdateInput dto) {
        XtdSubject subject = subjectRepository
                .findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("No Object with id " + dto.getId() + " not found."));

        logger.debug("Updating entity {}", subject);

        // general infos
        subject.setVersionId(dto.getVersionId());
        subject.setVersionDate(dto.getVersionDate());

        List<TextInput> nameDtos = dto.getNames();
        List<String> nameIds = nameDtos.stream().map(TextInput::getId).collect(Collectors.toList());
        List<XtdName> names = new ArrayList<>(subject.getNames());

        // empty current set to prepare for updates
        subject.getNames().clear();

        // remove deleted descriptions temporary list
        names.removeIf(x ->  nameIds.indexOf(x.getId()) == -1);

        for (int i = 0; i < nameDtos.size(); i++) {
            TextInput input = nameDtos.get(i);
            XtdName newName = toName(input);
            newName.setSortOrder(i);

            logger.debug("Transient name {}", newName);

            int index = names.indexOf(newName);
            if (input.getId() != null && (index > -1)) {
                // Update of an existing name entity
                XtdName oldName = names.get(index);

                logger.debug("Persistent name to be updated: {}", oldName);

                if (!oldName.getLanguageName().equals(newName.getLanguageName())) {
                    // Update of languageName is not allowed
                    throw new IllegalArgumentException("Update of languageName of name with id " + newName.getId() + " is not allowed.");
                }

                oldName.setName(newName.getName());
                oldName.setSortOrder(newName.getSortOrder());

                logger.debug("Updated persistent name: {}" , oldName);

                subject.getNames().add(oldName);
            } else {
            // New entity with no given id or preset id
                subject.getNames().add(newName);
            }
        }

        List<TextInput> descriptionDtos = dto.getDescriptions();
        List<String> descriptionIds = descriptionDtos.stream().map(TextInput::getId).collect(Collectors.toList());
        List<XtdDescription> descriptions = new ArrayList<>(subject.getDescriptions());

        // empty current set to prepare for updates
        subject.getDescriptions().clear();

        // remove deleted descriptions temporary list
        descriptions.removeIf(x -> descriptionIds.indexOf(x.getId()) == -1);

        for (int i = 0; i < descriptionDtos.size(); i++) {
            TextInput input = descriptionDtos.get(i);
            XtdDescription newDescription = toDescription(input);
            newDescription.setSortOrder(i);

            logger.debug("Transient description {}", newDescription);

            int index = descriptions.indexOf(newDescription);
            if (input.getId() != null && (index > -1)) {
                // Update of an existing entity
                XtdDescription oldDescription = descriptions.get(index);
                logger.debug("Persistent description to be updated: {}", oldDescription);

                if (!oldDescription.getLanguageName().equals(newDescription.getLanguageName())) {
                    // Update of languageName is not allowed
                    throw new IllegalArgumentException("Update of languageName of description with id " + newDescription.getId() + " is not allowed.");
                }

                oldDescription.setDescription(newDescription.getDescription());
                oldDescription.setSortOrder(newDescription.getSortOrder());

                logger.debug("Updated persistent description: {}" , oldDescription);

                subject.getDescriptions().add(oldDescription);
            } else {
                // New entity with no given id
                subject.getDescriptions().add(newDescription);
            }
        }

        logger.debug("New state {}", subject);

        return subjectRepository.save(subject);
    }

    @Transactional
    @Override
    public Optional<XtdSubject> delete(String id) {
        Optional<XtdSubject> subject = subjectRepository.findById(id);
        subject.ifPresent(x -> subjectRepository.delete(x));
        return subject;
    }

    @Override
    public Optional<XtdSubject> findById(String id) {
        return subjectRepository.findById(id);
    }

    @Override
    public Page<XtdSubject> findByIds(List<String> ids, Pageable pageable) {
		return subjectRepository.findAllById(ids, pageable);
    }

    @Override
    public Page<XtdSubject> findAll(Pageable pageable) {
        Iterable<XtdSubject> nodes = subjectRepository.findAll(pageable);
        List<XtdSubject> resultList = new ArrayList<>();
        nodes.forEach(resultList::add);
        return PageableExecutionUtils.getPage(resultList, pageable, subjectRepository::count);
    }

    @Override
    public Page<XtdSubject> findByTerm(String term, Pageable pageable) {
        return subjectRepository.findAllByTerm(term, pageable);
    }

    protected XtdSubject toEntity(RootInput input) {
        XtdSubject subject = new XtdSubject();
        subject.setId(input.getId());
        subject.setVersionId(input.getVersionId());
        subject.setVersionDate(input.getVersionDate());

        List<TextInput> names = input.getNames();
        for (int i = 0; i < names.size(); i++) {
            TextInput name = names.get(i);
            XtdName newName = toName(name);
            newName.setSortOrder(i);
            subject.getNames().add(newName);
        }

        List<TextInput> descriptions = input.getDescriptions();
        for (int i = 0; i < descriptions.size(); i++) {
            TextInput description = descriptions.get(i);
            XtdDescription newDescription = toDescription(description);
            newDescription.setSortOrder(i);
            subject.getDescriptions().add(newDescription);
        }

        return subject;
    }

    protected XtdName toName(TextInput input) {
        if (input.getId() != null) {
            return new XtdName(input.getId(), input.getLanguageCode(), input.getValue());
        }
        return new XtdName(input.getLanguageCode(), input.getValue());
    }

    protected XtdDescription toDescription(TextInput input) {
        if (input.getId() != null) {
            return new XtdDescription(input.getId(), input.getLanguageCode(), input.getValue());
        }
        return new XtdDescription(input.getLanguageCode(), input.getValue());
    }
}
