package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.XtdEntity;
import de.bentrm.datacat.query.FilterOptions;
import de.bentrm.datacat.repository.EntityRepository;
import de.bentrm.datacat.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
@Transactional(readOnly = true)
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private EntityRepository repository;

    @Override
    public Page<XtdEntity> search(FilterOptions<String> filterOptions, Pageable pageable) {
        return repository.search(filterOptions, pageable);
    }

    @Override
    public long countSearchResults(@NotNull FilterOptions<String> filterOptions) {
        return repository.count(filterOptions);
    }
}
