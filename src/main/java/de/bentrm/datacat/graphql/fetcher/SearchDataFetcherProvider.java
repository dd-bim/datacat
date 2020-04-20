package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.domain.XtdEntity;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.PagingOptions;
import de.bentrm.datacat.query.SearchOptions;
import de.bentrm.datacat.service.SearchService;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SearchDataFetcherProvider {

    @Autowired
    private SearchService searchService;

    public Map<String, DataFetcher> getQueryDataFetchers() {
        return Map.ofEntries(
                Map.entry("search", search())
        );
    }

    public DataFetcher<Connection<XtdEntity>> search() {
        return environment -> {
            ObjectMapper mapper = new ObjectMapper();

            Map<String, Object> optionsInput = environment.getArgument("options");
            SearchOptions<String> searchOptions = mapper.convertValue(optionsInput, SearchOptions.class);
            if (searchOptions == null) searchOptions = new SearchOptions<>();

            Map<String, Object> pagingInput = environment.getArgument("paging");
            PagingOptions pagingOptions = mapper.convertValue(pagingInput, PagingOptions.class);
            if (pagingOptions == null) pagingOptions = PagingOptions.defaults();

            Page<XtdEntity> page = searchService.search(searchOptions, pagingOptions.getPageble());
            return new Connection<>(page);
        };
    }

}
