package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.CatalogItem;
import de.bentrm.datacat.catalog.service.CatalogService;
import de.bentrm.datacat.catalog.service.value.HierarchyValue;
import de.bentrm.datacat.catalog.specification.CatalogItemSpecification;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.SpecificationMapper;
import de.bentrm.datacat.graphql.input.ApiInputMapper;
import de.bentrm.datacat.graphql.input.HierarchyFilterInput;
import de.bentrm.datacat.graphql.input.HierarchyRootNodeFilterInput;
import de.bentrm.datacat.graphql.input.SearchInput;
import graphql.schema.DataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class SearchFetchers implements QueryFetchers {

    final int DEFAULT_HIERARCHY_DEPTH = 10;

    @Autowired
    private ApiInputMapper inputMapper;

    @Autowired
    private SpecificationMapper specificationMapper;

    @Autowired
    private CatalogService catalogService;

    @Override
    public Map<String, DataFetcher> getQueryFetchers() {
        return Map.ofEntries(
                Map.entry("search", search()),
                Map.entry("hierarchy", hierarchy())
        );
    }

    public DataFetcher<Connection<CatalogItem>> search() {
        return environment -> {
            Map<String, Object> argument = environment.getArgument("input");
            SearchInput searchInput = inputMapper.toSearchInput(argument);
            if (searchInput == null) searchInput = new SearchInput();

            CatalogItemSpecification spec = specificationMapper.toCatalogItemSpecification(searchInput);

            if (environment.getSelectionSet().containsAnyOf("nodes/*", "pageInfo/*")) {
                Page<CatalogItem> page = catalogService.findAllCatalogItems(spec);
                return Connection.of(page);
            } else {
                long totalElements = catalogService.countCatalogItems(spec);
                return Connection.empty(totalElements);
            }

        };
    }

    public DataFetcher<HierarchyValue> hierarchy() {
        return environment -> {
            Map<String, Object> argument = environment.getArgument("input");
            final HierarchyFilterInput input = inputMapper.toHierarchyFilterInput(argument);
            final HierarchyRootNodeFilterInput rootNodeFilter = input.getRootNodeFilter();
            final CatalogItemSpecification rootNodeSpecification = specificationMapper.toCatalogItemSpecification(rootNodeFilter);
            return catalogService.getHierarchy(rootNodeSpecification, DEFAULT_HIERARCHY_DEPTH);
        };
    };
}
