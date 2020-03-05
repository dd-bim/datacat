package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.NamedEntity;
import de.bentrm.datacat.domain.XtdDescription;
import de.bentrm.datacat.domain.XtdName;
import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.dto.XtdDescriptionInputDto;
import de.bentrm.datacat.dto.XtdNameInputDto;
import de.bentrm.datacat.dto.XtdRootDto;
import de.bentrm.datacat.repository.LanguageRepository;
import de.bentrm.datacat.repository.NamedEntityRepository;
import de.bentrm.datacat.service.NamedEntityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public abstract class NamedEntityServiceImpl<T extends NamedEntity, R extends NamedEntityRepository<T>> implements NamedEntityService<T> {

    protected LanguageRepository languageRepository;

    protected R repository;

    public NamedEntityServiceImpl(LanguageRepository languageRepository, R repository) {
        this.languageRepository = languageRepository;
        this.repository = repository;
    }

    public R getRepository() {
        return repository;
    }

    protected void mapInputToObject(XtdRoot newObject, XtdRootDto dto) {
        newObject.setId(dto.getId());
        for (int i = 0; i < dto.getNames().size(); i++) {
            XtdNameInputDto nameDto = dto.getNames().get(i);
            XtdName newName = new XtdName();
            var language = languageRepository.findById(nameDto.getLanguageCode());
            newName.setId(nameDto.getId());
            newName.setLanguageName(language.get());
            newName.setName(nameDto.getName());
            newName.setSortOrder(i);
            newObject.getNames().add(newName);
        }
        for (int i = 0; i < dto.getDescriptions().size(); i++) {
            XtdDescriptionInputDto descriptionDto = dto.getDescriptions().get(i);
            XtdDescription newDescription = new XtdDescription();
            var language = languageRepository.findById(descriptionDto.getLanguageCode());
            newDescription.setId(descriptionDto.getId());
            newDescription.setLanguageName(language.get());
            newDescription.setDescription(descriptionDto.getDescription());
            newDescription.setSortOrder(i);
            newObject.getDescriptions().add(newDescription);
        }
    }
}
