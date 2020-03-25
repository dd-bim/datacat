package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.XtdExternalDocument;
import de.bentrm.datacat.domain.XtdName;
import de.bentrm.datacat.graphql.dto.ExternalDocumentInput;
import de.bentrm.datacat.repository.ExternalDocumentRepository;
import de.bentrm.datacat.service.ExternalDocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Validated
public class ExternalDocumentServiceImpl implements ExternalDocumentService {

    Logger logger = LoggerFactory.getLogger(ExternalDocumentServiceImpl.class);

    @Autowired
    private ExternalDocumentRepository entityRepository;

    @Transactional
    @Override
    public XtdExternalDocument create(ExternalDocumentInput dto) {
        XtdExternalDocument newDocument = new XtdExternalDocument();
        if (dto.hasId()) {
            newDocument.setId(dto.getId());
        }
        for (int i = 0; i < dto.getNames().size(); i++) {
            var nameDto = dto.getNames().get(i);
            XtdName newName = new XtdName();
            if (nameDto.getId() != null && !nameDto.getId().isBlank()) {
                newName.setId(nameDto.getId());
            }
            newName.setLanguageName(nameDto.getLanguageCode());
            newName.setName(nameDto.getValue());
            newName.setSortOrder(i);
            newDocument.getNames().add(newName);
        }

        return entityRepository.save(newDocument);
    }

    @Transactional
    @Override
    public Optional<XtdExternalDocument> delete(String id) {
        Optional<XtdExternalDocument> document = entityRepository.findById(id);
        document.ifPresent(x -> entityRepository.delete(x));
        return document;
    }

    @Override
    public Optional<XtdExternalDocument> findById(String id) {
        return entityRepository.findById(id);
    }

    @Override
    public Page<XtdExternalDocument> findByIds(@NotNull List<String> ids, @NotNull Pageable pageable) {
        logger.warn("Unimplemented method: findByIds");
        return null;
    }

    @Override
    public Page<XtdExternalDocument> findAll(Pageable pageable) {
        return entityRepository.findAll(pageable);
    }

    @Override
    public Page<XtdExternalDocument> findByTerm(String term, Pageable pageable) {
        logger.warn("Unimplemented method: findByTerm");
        return null;
    }
}
