package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.XtdLanguage;
import de.bentrm.datacat.repository.LanguageRepository;
import de.bentrm.datacat.service.XtdLanguageService;
import de.bentrm.datacat.dto.XtdLanguageInputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;

@Service
@Transactional
public class XtdLanguageServiceImpl implements XtdLanguageService {

    private final LanguageRepository repository;

    @Autowired
    public XtdLanguageServiceImpl(LanguageRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public XtdLanguage findById(String id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public XtdLanguage findByLanguageRepresentationId(String id) {
        return repository.findByLanguageRepresentationId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<XtdLanguage> findAll() {
        return repository.findAll(Pageable.unpaged());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<XtdLanguage> findAll(int pageNumber, int pageSize) {
        return repository.findAll(PageRequest.of(pageNumber, pageSize));
    }

    @Override
    public XtdLanguage create(@NotNull XtdLanguageInputDto dto) {
        XtdLanguage newLanguage = new XtdLanguage();
        newLanguage.setId(dto.getId());
        newLanguage.setLanguageCode(dto.getLanguageCode());
        newLanguage.setLanguageNameInEnglish(dto.getLanguageNameInEnglish());
        newLanguage.setLanguageNameInSelf(dto.getLanguageNameInSelf());
        return repository.save(newLanguage);
    }
}
