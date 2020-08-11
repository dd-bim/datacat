package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.domain.CatalogItem;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.CatalogStatistics;
import de.bentrm.datacat.graphql.dto.InputMapper;
import de.bentrm.datacat.graphql.dto.SearchInput;
import de.bentrm.datacat.service.CatalogService;
import de.bentrm.datacat.specification.CatalogItemSpecification;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class BaseFetchers implements QueryFetchers {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private CatalogService catalogService;


    @Override
    public Map<String, DataFetcher> getQueryFetchers() {
        return Map.ofEntries(
                Map.entry("statistics", statistics()),
                Map.entry("node", node()),
                Map.entry("search", search())
        );
    }

    public DataFetcher<CatalogStatistics> statistics() {
        return environment -> catalogService.getStatistics();
    }

    public DataFetcher<Optional<CatalogItem>> node() {
        return environment -> {
            String id = environment.getArgument("id");
            return catalogService.getCatalogItem(id);
        };
    }

    public DataFetcher<Connection<CatalogItem>> search() {
        return env -> {
            Map<String, Object> input = env.getArgument("input");
            SearchInput searchInput = objectMapper.convertValue(input, SearchInput.class);
            if (searchInput == null) searchInput = new SearchInput();

            CatalogItemSpecification spec = InputMapper.INSTANCE.toCatalogItemSpecification(searchInput);

            if (env.getSelectionSet().containsAnyOf("nodes/*", "pageInfo/*")) {
                Page<CatalogItem> page = catalogService.findAllCatalogItems(spec);
                return Connection.of(page);
            } else {
                long totalElements = catalogService.countCatalogItems(spec);
                return Connection.empty(totalElements);
            }

        };
    }

}
