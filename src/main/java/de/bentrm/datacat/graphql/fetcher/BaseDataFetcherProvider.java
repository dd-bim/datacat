package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.domain.CatalogItem;
import de.bentrm.datacat.domain.XtdLanguage;
import de.bentrm.datacat.domain.XtdLanguageRepresentation;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.PageInfo;
import de.bentrm.datacat.graphql.dto.DtoMapper;
import de.bentrm.datacat.graphql.dto.SearchInput;
import de.bentrm.datacat.service.CatalogService;
import de.bentrm.datacat.service.LanguageService;
import de.bentrm.datacat.service.Specification;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class BaseDataFetcherProvider implements QueryDataFetcherProvider {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private DtoMapper dtoMapper;

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private LanguageService languageService;

    @Override
    public Map<String, DataFetcher> getQueryDataFetchers() {
        return Map.ofEntries(
                Map.entry("node", node()),
                Map.entry("search", search())
        );
    }

    public DataFetcher<Optional<CatalogItem>> node() {
        return environment -> {
            String id = environment.getArgument("id");
            return catalogService.getCatalogItem(id);
        };
    }

    public DataFetcher<Connection<CatalogItem>> search() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            SearchInput filterInput = objectMapper.convertValue(input, SearchInput.class);
            if (filterInput == null) filterInput = new SearchInput();

//            DataFetchingFieldSelectionSet selectionSet = environment.getSelectionSet();
//
//            if (selectionSet.containsAnyOf("nodes/*", "pageInfo/*")) {
//                Page<CatalogItem> page = catalogService.searchCatalogItem(filterInput, pagingOptions.getPageble());
//                return new Connection<>(page.getContent(), PageInfo.of(page), page.getTotalElements());
//            } else {
//                long totalElements = catalogService.countSearchResults(filterInput);
//                return new Connection<>(null, null, totalElements);
//            }

            Specification spec = dtoMapper.toSpecification(filterInput);
            Page<CatalogItem> page = catalogService.searchCatalogItem(spec);
            return new Connection<>(page.getContent(), PageInfo.of(page), page.getTotalElements());
        };
    }

    public DataFetcher<Optional<XtdLanguage>> languageByLanguageRepresentation() {
        return environment -> {
            XtdLanguageRepresentation value = environment.getSource();
            return languageService.findByLanguage(value.getLanguageName());
        };
    }

}
