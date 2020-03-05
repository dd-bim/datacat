package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.XtdDescription;
import de.bentrm.datacat.domain.XtdLanguage;
import de.bentrm.datacat.domain.XtdName;
import de.bentrm.datacat.domain.XtdSubject;
import de.bentrm.datacat.dto.RootInputDto;
import de.bentrm.datacat.repository.LanguageRepository;
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

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class SubjectServiceImpl implements SubjectService {

    Logger logger = LoggerFactory.getLogger(SubjectServiceImpl.class);

    @Autowired
    private Validator validator;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Override
    public XtdSubject create(RootInputDto dto) {
        Set<ConstraintViolation<RootInputDto>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<RootInputDto> violation : violations) {
                logger.error(violation.getMessage());
            }
            throw new ConstraintViolationException(violations);
        }

        XtdSubject newSubject = new XtdSubject();
        if (dto.getId() != null && !dto.getId().isBlank()) {
            newSubject.setId(dto.getId());
        }
        newSubject.setVersionId(dto.getVersionId());
        newSubject.setVersionDate(dto.getVersionDate());

        for (int i = 0; i < dto.getNames().size(); i++) {
            var nameDto = dto.getNames().get(i);

            XtdLanguage language = languageRepository
                    .findById(nameDto.getLanguageCode())
                    .orElseThrow(() -> new IllegalArgumentException("Unsupported language code: " + nameDto.getLanguageCode()));

            XtdName newName = new XtdName();
            if (nameDto.getId() != null && !nameDto.getId().isBlank()) {
                newName.setId(nameDto.getId());
            }
            newName.setLanguageName(language);
            newName.setName(nameDto.getValue());
            newName.setSortOrder(i);
            newSubject.getNames().add(newName);
        }

        if (dto.getDescriptions() != null) {
            for (int i = 0; i < dto.getDescriptions().size(); i++) {
                var descriptionDto = dto.getDescriptions().get(i);

                XtdLanguage language = languageRepository
                        .findById(descriptionDto.getLanguageCode())
                        .orElseThrow(() -> new IllegalArgumentException("Unsupported language code: " + descriptionDto.getLanguageCode()));

                XtdDescription newDescription = new XtdDescription();
                if (descriptionDto.getId() != null && !descriptionDto.getId().isBlank())
                newDescription.setId(descriptionDto.getId());
                newDescription.setLanguageName(language);
                newDescription.setDescription(descriptionDto.getValue());
                newDescription.setSortOrder(i);
                newSubject.getDescriptions().add(newDescription);
            }
        }

        return subjectRepository.save(newSubject);
    }

    @Override
    public Optional<XtdSubject> delete(String id) {
        Optional<XtdSubject> document = subjectRepository.findById(id);
        document.ifPresent(x -> subjectRepository.delete(x));
        return document;
    }

    @Override
    public Optional<XtdSubject> findById(String id) {
        return subjectRepository.findById(id);
    }

    @Override
    public Page<XtdSubject> findAll(Pageable pageable) {
        Iterable<XtdSubject> nodes = subjectRepository.findAll(pageable.getOffset(), pageable.getPageSize());
        List<XtdSubject> resultList = new ArrayList<>();
        nodes.forEach(resultList::add);
        return PageableExecutionUtils.getPage(resultList, pageable, subjectRepository::count);

    }

    @Override
    public Page<XtdSubject> findByTerm(String match, Pageable pageable) {
        Iterable<XtdSubject> nodes = subjectRepository.findByTerm(match, pageable.getOffset(), pageable.getPageSize());
        List<XtdSubject> resultList = new ArrayList<>();
        nodes.forEach(resultList::add);
        return PageableExecutionUtils.getPage(resultList, pageable, () -> subjectRepository.countByTerm(match));
    }
}
