package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.XtdExternalDocument;
import de.bentrm.datacat.domain.XtdName;
import de.bentrm.datacat.dto.ExternalDocumentInputDto;
import de.bentrm.datacat.repository.ExternalDocumentRepository;
import de.bentrm.datacat.repository.LanguageRepository;
import de.bentrm.datacat.service.ExternalDocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class ExternalDocumentServiceImpl implements ExternalDocumentService {

    Logger logger = LoggerFactory.getLogger(ExternalDocumentServiceImpl.class);

    @Autowired
    private Validator validator;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private ExternalDocumentRepository externalDocumentRepository;

    @Override
    public XtdExternalDocument create(ExternalDocumentInputDto dto) {
        Set<ConstraintViolation<ExternalDocumentInputDto>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<ExternalDocumentInputDto> violation : violations) {
                logger.error(violation.getMessage());
            }
            throw new ConstraintViolationException(violations);
        }

        XtdExternalDocument newDocument = new XtdExternalDocument();
        newDocument.setId(dto.getId());
        for (int i = 0; i < dto.getNames().size(); i++) {
            var nameDto = dto.getNames().get(i);
            XtdName newName = new XtdName();
            var language = languageRepository.findById(nameDto.getLanguageCode());
            newName.setId(nameDto.getId());
            newName.setLanguageName(language.get());
            newName.setName(nameDto.getValue());
            newName.setSortOrder(i);
            newDocument.getNames().add(newName);
        }

        return externalDocumentRepository.save(newDocument);
    }

    @Override
    public Optional<XtdExternalDocument> delete(String id) {
        Optional<XtdExternalDocument> document = externalDocumentRepository.findById(id);
        document.ifPresent(x -> externalDocumentRepository.delete(x));
        return document;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<XtdExternalDocument> findById(String id) {
        return externalDocumentRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<XtdExternalDocument> findAll(Pageable pageable) {
        return externalDocumentRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<XtdExternalDocument> findByTerm(String match, Pageable pageable) {
        logger.warn("Unimplemented method: findByTerm");
        return null;
    }
}
