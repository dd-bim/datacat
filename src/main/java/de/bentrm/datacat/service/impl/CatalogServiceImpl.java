package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.CatalogItem;
import de.bentrm.datacat.domain.Entity;
import de.bentrm.datacat.query.FilterOptions;
import de.bentrm.datacat.repository.CatalogItemRepository;
import de.bentrm.datacat.repository.EntityRepository;
import de.bentrm.datacat.service.CatalogService;
import de.bentrm.datacat.service.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Validated
@Transactional(readOnly = true)
@Service
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    private EntityRepository entityRepository;

    @Autowired
    private CatalogItemRepository catalogItemRepository;


    @Override
    public @NotNull Optional<Entity> getEntity(@NotBlank String id) {
        return entityRepository.findById(id);
    }

    @Override
    public @NotNull Optional<CatalogItem> getCatalogItem(@NotBlank String id) {
        return catalogItemRepository.findById(id);
    }

    @Override
    public @NotNull Page<CatalogItem> searchCatalogItem(@NotNull Specification specification) {
        return catalogItemRepository.findAll(specification);
    }

    @Override
    public Page<CatalogItem> searchCatalogItem(FilterOptions filterOptions, Pageable pageable) {
        return catalogItemRepository.search(filterOptions, pageable);
    }

    @Override
    public long countSearchResults(@NotNull FilterOptions filterOptions) {
        return catalogItemRepository.count(filterOptions);
    }
}
