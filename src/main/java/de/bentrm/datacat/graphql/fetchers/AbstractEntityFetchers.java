package de.bentrm.datacat.graphql.fetchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.base.domain.Entity;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import de.bentrm.datacat.catalog.service.CatalogService;
import de.bentrm.datacat.catalog.service.QueryService;
import de.bentrm.datacat.catalog.specification.CatalogItemSpecification;
import de.bentrm.datacat.catalog.specification.RootSpecification;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.FilterInput;
import de.bentrm.datacat.graphql.dto.SpecificationMapper;
import de.bentrm.datacat.graphql.fetcher.DescriptionFetcher;
import de.bentrm.datacat.graphql.fetcher.NameFetcher;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class AbstractEntityFetchers<
        T extends Entity,
        S extends QueryService<T>>
        implements AttributeFetchers, QueryFetchers {

    protected final ObjectMapper objectMapper = new ObjectMapper();
    protected final S entityService;
    protected CatalogService catalogService;

    public AbstractEntityFetchers(S entityService) {
        this.entityService = entityService;
    }

    public abstract String getFetcherName();

    public abstract String getListFetcherName();

    public abstract String getMutationNameSuffix();

    @Autowired
    public void setCatalogService(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @Override
    public Map<String, DataFetcher> getQueryFetchers() {
        final Map<String, DataFetcher> fetchers = new HashMap<>();
        fetchers.put(getFetcherName(), fetchOne());
        fetchers.put(getListFetcherName(), fetchAll());
        return fetchers;
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        Map<String, DataFetcher> fetchers = new HashMap<>();
        fetchers.put("name", new NameFetcher());
        fetchers.put("description", new DescriptionFetcher());
        return fetchers;
    }

    protected DataFetcher<T> fetchOne() {
        return environment -> {
            String id = environment.getArgument("id");
            return entityService.findById(id).orElseThrow();
        };
    }

    public DataFetcher<Connection<T>> fetchAll() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            FilterInput filterInput = objectMapper.convertValue(input, FilterInput.class);
            if (filterInput == null) filterInput = new FilterInput();

            CatalogItemSpecification specification = SpecificationMapper.INSTANCE.toCatalogItemSpecification(filterInput);
            if (environment.getSelectionSet().containsAnyOf("nodes/*", "pageInfo/*")) {
                Page<T> page = entityService.findAll(specification);
                return Connection.of(page);
            } else {
                long count = entityService.count(specification);
                return Connection.empty(count);
            }
        };
    }

    protected Connection<XtdRoot> fetchRootConnection(DataFetchingEnvironment environment, RootSpecification specification) {
        if (!environment.getSelectionSet().containsAnyOf("nodes/*", "pageInfo/*")) {
            final @NotNull long count = catalogService.countRootItems(specification);
            return Connection.empty(count);
        }
        @NotNull final Page<XtdRoot> relatedThings = catalogService.findAllRootItems(specification);
        return Connection.of(relatedThings);
    }

}
