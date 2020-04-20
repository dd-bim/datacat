package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.XtdEntity;
import de.bentrm.datacat.query.SearchOptions;
import de.bentrm.datacat.repository.EntityRepository;
import de.bentrm.datacat.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Validated
@Transactional(readOnly = true)
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private EntityRepository repository;

    @Override
    public Page<XtdEntity> search(SearchOptions<String> searchOptions, Pageable pageable) {
        return repository.search(searchOptions, pageable);
    }
}
