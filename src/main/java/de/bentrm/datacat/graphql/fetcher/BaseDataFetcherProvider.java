package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.domain.XtdEntity;
import de.bentrm.datacat.domain.XtdLanguage;
import de.bentrm.datacat.domain.XtdLanguageRepresentation;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.PageInfo;
import de.bentrm.datacat.graphql.dto.PagingOptions;
import de.bentrm.datacat.query.FilterOptions;
import de.bentrm.datacat.service.LanguageService;
import de.bentrm.datacat.service.SearchService;
import de.bentrm.datacat.service.SingleEntityService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingFieldSelectionSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class BaseDataFetcherProvider implements QueryDataFetcherProvider {

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private SingleEntityService singleEntityService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private LanguageService languageService;

    @Override
    public Map<String, DataFetcher> getQueryDataFetchers() {
        return Map.ofEntries(
                Map.entry("node", node()),
                Map.entry("search", search())
        );
    }

    public DataFetcher<Optional<XtdEntity>> node() {
        return environment -> {
            String id = environment.getArgument("id");
            return singleEntityService.findById(id);
        };
    }

    public DataFetcher<Connection<XtdEntity>> search() {
        return environment -> {
            Map<String, Object> optionsInput = environment.getArgument("options");
            FilterOptions<String> searchOptions = mapper.convertValue(optionsInput, FilterOptions.class);
            if (searchOptions == null) searchOptions = new FilterOptions<>();

            Map<String, Object> pagingInput = environment.getArgument("paging");
            PagingOptions pagingOptions = mapper.convertValue(pagingInput, PagingOptions.class);
            if (pagingOptions == null) pagingOptions = PagingOptions.defaults();

            DataFetchingFieldSelectionSet selectionSet = environment.getSelectionSet();

            if (selectionSet.containsAnyOf("nodes/*", "pageInfo/*")) {
                Page<XtdEntity> page = searchService.search(searchOptions, pagingOptions.getPageble());
                return new Connection<>(page.getContent(), PageInfo.of(page), page.getTotalElements());
            } else {
                long totalElements = searchService.countSearchResults(searchOptions);
                return new Connection<>(null, null, totalElements);
            }
        };
    }

    public DataFetcher<Optional<XtdLanguage>> languageByLanguageRepresentation() {
        return environment -> {
            XtdLanguageRepresentation value = environment.getSource();
            return languageService.findByLanguage(value.getLanguageName());
        };
    }

}
