package de.bentrm.datacat.graphql.fetcher.verification;

import de.bentrm.datacat.catalog.domain.CatalogRecord;
import de.bentrm.datacat.catalog.service.CatalogSearchService;
import de.bentrm.datacat.catalog.service.CatalogVerificationService;
import de.bentrm.datacat.catalog.service.value.verification.findMissingEnglishDescriptionValue;
import de.bentrm.datacat.catalog.specification.CatalogRecordSpecification;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.SpecificationMapper;
import de.bentrm.datacat.graphql.fetcher.QueryFetchers;
import de.bentrm.datacat.graphql.input.ApiInputMapper;
import de.bentrm.datacat.graphql.input.SearchInput;
import de.bentrm.datacat.graphql.input.verification.findMissingEnglishDescriptionFilterInput;
import de.bentrm.datacat.graphql.input.verification.findMissingEnglishDescriptionNodeTypeFilterInput;
import graphql.schema.DataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class findMissingEnglishDescriptionFetchers implements QueryFetchers {

    @Autowired
    private ApiInputMapper inputMapper;

    @Autowired
    private SpecificationMapper specificationMapper;

    @Autowired
    private CatalogVerificationService catalogVerificationService;

    @Autowired
    private CatalogSearchService catalogSearchService;

    @Override
    public Map<String, DataFetcher> getQueryFetchers() {
        return Map.ofEntries(
                Map.entry("search", search()),
                Map.entry("findMissingEnglishDescription", findMissingEnglishDescription())
        );
    }

    public DataFetcher<Connection<CatalogRecord>> search() {
        return environment -> {
            Map<String, Object> argument = environment.getArgument("input");
            SearchInput searchInput = inputMapper.toSearchInput(argument);

            if (searchInput == null) searchInput = new SearchInput();

            Integer pageSize = environment.getArgument("pageSize");
            if (pageSize != null) searchInput.setPageSize(pageSize);

            Integer pageNumber = environment.getArgument("pageNumber");
            if (pageNumber != null) searchInput.setPageNumber(pageNumber);

            CatalogRecordSpecification spec = specificationMapper.toCatalogRecordSpecification(searchInput);

            if (environment.getSelectionSet().containsAnyOf("nodes/*", "pageInfo/*")) {
                Page<CatalogRecord> page = catalogSearchService.search(spec);
                return Connection.of(page);
            } else {
                long totalElements = catalogSearchService.count(spec);
                return Connection.empty(totalElements);
            }

        };
    }

    public DataFetcher<findMissingEnglishDescriptionValue> findMissingEnglishDescription() {
        return environment -> {
            Map<String, Object> argument = environment.getArgument("input");
            final findMissingEnglishDescriptionFilterInput input = inputMapper.tofindMissingEnglishDescriptionFilterInput(argument);
            final findMissingEnglishDescriptionNodeTypeFilterInput nodeTypeFilter = input.getNodeTypeFilter();
            final CatalogRecordSpecification catalogEntryType = specificationMapper.toCatalogRecordSpecification(nodeTypeFilter);
            return catalogVerificationService.getfindMissingEnglishDescription(catalogEntryType);
        };
    }

    ;
}
