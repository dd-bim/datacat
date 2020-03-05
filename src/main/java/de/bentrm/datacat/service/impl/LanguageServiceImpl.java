package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.XtdLanguage;
import de.bentrm.datacat.dto.LanguageInputDto;
import de.bentrm.datacat.repository.LanguageRepository;
import de.bentrm.datacat.service.LanguageService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class LanguageServiceImpl implements LanguageService {

    Log logger = LogFactory.getLog(LanguageServiceImpl.class);

    @Autowired
    private Validator validator;

    @Autowired
    private LanguageRepository languageRepository;

    @Override
    public XtdLanguage create(@Valid LanguageInputDto dto) throws ConstraintViolationException {
        Set<ConstraintViolation<LanguageInputDto>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<LanguageInputDto> violation : violations) {
                logger.error(violation.getMessage());
            }
            throw new ConstraintViolationException(violations);
        }

        XtdLanguage newLanguage = new XtdLanguage();
        newLanguage.setId(dto.getId());
        newLanguage.setLanguageNameInEnglish(dto.getLanguageNameInEnglish());
        newLanguage.setLanguageNameInSelf(dto.getLanguageNameInSelf());

        return languageRepository.save(newLanguage);
    }

    @Override
    public Optional<XtdLanguage> delete(String id) {
        Optional<XtdLanguage> language = languageRepository.findById(id);
        language.ifPresent(x -> languageRepository.delete(x));
        return language;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<XtdLanguage> findById(String id) {
        return languageRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<XtdLanguage> findByLanguageRepresentationId(String id) {
        return languageRepository.findByLanguageRepresentationId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<XtdLanguage> findAll(Pageable pageable) {
        return languageRepository.findAll(pageable);
    }

    @Override
    public Page<XtdLanguage> findByTerm(String match, Pageable pageable) {
        logger.warn("Unimplemented method: findByTerm");
        return null;
    }
}
