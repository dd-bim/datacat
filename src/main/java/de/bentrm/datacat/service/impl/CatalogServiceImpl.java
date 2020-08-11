package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.CatalogItem;
import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.graphql.dto.CatalogItemStatistics;
import de.bentrm.datacat.graphql.dto.CatalogStatistics;
import de.bentrm.datacat.repository.CatalogItemRepository;
import de.bentrm.datacat.repository.RootRepository;
import de.bentrm.datacat.service.CatalogService;
import de.bentrm.datacat.specification.CatalogItemSpecification;
import de.bentrm.datacat.specification.RootSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Optional;

@Validated
@Transactional(readOnly = true)
@Service
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    private CatalogItemRepository catalogItemRepository;

    @Autowired
    private RootRepository rootRepository;

    @Override
    public CatalogStatistics getStatistics() {
        final Map<String, Long> labelsStats = catalogItemRepository.statistics();
        final CatalogStatistics statistics = new CatalogStatistics();
        labelsStats.forEach((label, count) -> {
            if (label.startsWith("Xtd")) {
                statistics.getItems().add(new CatalogItemStatistics(label, count));
            }
        });
        return statistics;
    }

    @Override
    public @NotNull Optional<CatalogItem> getCatalogItem(@NotBlank String id) {
        return catalogItemRepository.findById(id);
    }

    @Override
    public @NotNull Optional<XtdRoot> getRootItem(@NotNull String id) {
        return rootRepository.findById(id);
    }

    @Override
    public @NotNull Page<CatalogItem> findAllCatalogItems(@NotNull CatalogItemSpecification specification) {
        return catalogItemRepository.findAll(specification);
    }

    @Override
    public long countCatalogItems(@NotNull CatalogItemSpecification specification) {
        return catalogItemRepository.count(specification);
    }

    @Override
    public @NotNull Page<XtdRoot> findAllRootItems(@NotNull RootSpecification specification) {
        return rootRepository.findAll(specification);
    }

    @Override
    public long countRootItems(@NotNull RootSpecification specification) {
        return rootRepository.count(specification);
    }
}
