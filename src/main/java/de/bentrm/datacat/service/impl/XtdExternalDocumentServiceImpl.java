package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.XtdExternalDocument;
import de.bentrm.datacat.domain.XtdLanguage;
import de.bentrm.datacat.domain.XtdName;
import de.bentrm.datacat.repository.ExternalDocumentRepository;
import de.bentrm.datacat.repository.LanguageRepository;
import de.bentrm.datacat.service.XtdExternalDocumentService;
import de.bentrm.datacat.dto.XtdExternalDocumentInputDto;
import de.bentrm.datacat.dto.XtdNameInputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class XtdExternalDocumentServiceImpl extends NamedEntityServiceImpl<XtdExternalDocument, ExternalDocumentRepository> implements XtdExternalDocumentService {

    @Autowired
    public XtdExternalDocumentServiceImpl(LanguageRepository languageRepository, ExternalDocumentRepository repository) {
        super(languageRepository, repository);
    }

    public XtdExternalDocument create(XtdExternalDocumentInputDto dto) {
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
}
