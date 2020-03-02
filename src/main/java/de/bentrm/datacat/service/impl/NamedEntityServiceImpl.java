package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.*;
import de.bentrm.datacat.dto.XtdDescriptionInputDto;
import de.bentrm.datacat.dto.XtdNameInputDto;
import de.bentrm.datacat.dto.XtdRootDto;
import de.bentrm.datacat.repository.LanguageRepository;
import de.bentrm.datacat.repository.NamedEntityRepository;
import de.bentrm.datacat.service.NamedEntityService;
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

    @Override
    @Transactional(readOnly = true)
    public T findById(String id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<T> findAll(String label, int pageNumber, int pageSize) {
        int skip = pageSize * pageNumber;
        Iterable<T> queryResults = repository.findAll(label, skip, pageSize);
        int totalCount = repository.countFindAllResults(label);

        List<T> pageResults = new ArrayList<>();
        queryResults.forEach(pageResults::add);
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, "name");
        return new PageImpl<>(pageResults, pageRequest, totalCount);
    }

    @Override
    public Page<T> findByMatch(String label, String match, int pageNumber, int pageSize) {
        int skip = pageSize * pageNumber;
        Iterable<T> queryResults = repository.searchByNameAndDescription(label, match, skip, pageSize);
        int totalCount = repository.countSearchByNameAndDescriptionResults(label, match);

        List<T> results = new ArrayList<>();
        queryResults.forEach(results::add);
        return new PageImpl<>(results, PageRequest.of(pageNumber, pageSize), totalCount);
    }

    protected void mapInputToObject(XtdRoot newObject, XtdRootDto dto) {
        newObject.setId(dto.getId());
        for (int i = 0; i < dto.getNames().size(); i++) {
            XtdNameInputDto nameDto = dto.getNames().get(i);
            XtdName newName = new XtdName();
            XtdLanguage language = languageRepository.findByLanguageCode(nameDto.getLanguageCode());
            newName.setId(nameDto.getId());
            newName.setLanguageName(language);
            newName.setName(nameDto.getName());
            newName.setSortOrder(i);
            newObject.getNames().add(newName);
        }
        for (int i = 0; i < dto.getDescriptions().size(); i++) {
            XtdDescriptionInputDto descriptionDto = dto.getDescriptions().get(i);
            XtdDescription newDescription = new XtdDescription();
            XtdLanguage language = languageRepository.findByLanguageCode(descriptionDto.getLanguageCode());
            newDescription.setId(descriptionDto.getId());
            newDescription.setLanguageName(language);
            newDescription.setDescription(descriptionDto.getDescription());
            newDescription.setSortOrder(i);
            newObject.getDescriptions().add(newDescription);
        }
    }
}
