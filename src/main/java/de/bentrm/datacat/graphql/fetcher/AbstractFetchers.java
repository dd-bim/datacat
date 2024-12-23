package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.base.domain.Entity;
import de.bentrm.datacat.catalog.domain.CatalogRecord;
import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.service.QueryService;
import de.bentrm.datacat.catalog.specification.CatalogRecordSpecification;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.FilterInput;
import de.bentrm.datacat.graphql.dto.SpecificationMapper;
import graphql.schema.DataFetcher;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;

import java.util.Map;
import java.util.Optional;

@Getter
@Slf4j
public abstract class AbstractFetchers<T extends Entity>
        implements AttributeFetchers, QueryFetchers {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final NameFetcher nameFetcher = new NameFetcher();
    private final CommentFetcher commentFetcher = new CommentFetcher();

    private final DataFetcher<Optional<T>> fetchOne;
    private final DataFetcher<Connection<T>> fetchAll;

    public AbstractFetchers(QueryService<T> queryService) {
        this.fetchOne = environment -> {
            String id = environment.getArgument("id");
            return queryService.findById(id);
        };

        this.fetchAll = environment -> {
            Map<String, Object> input = environment.getArgument("input");
            FilterInput filterInput = objectMapper.convertValue(input, FilterInput.class);
            if (filterInput == null) filterInput = new FilterInput();

            CatalogRecordSpecification specification = SpecificationMapper.INSTANCE.toCatalogRecordSpecification(filterInput);
            if (environment.getSelectionSet().containsAnyOf("nodes/*", "pageInfo/*")) {
                Page<T> page = queryService.findAll(specification);
                return Connection.of(page);
            } else {
                long count = queryService.count(specification);
                return Connection.empty(count);
            }
        };
    }

    public abstract String getFetcherName();

    public abstract String getListFetcherName();

    @Override
    public Map<String, DataFetcher> getQueryFetchers() {
        return Map.of(
                getFetcherName(), fetchOne,
                getListFetcherName(), fetchAll
        );
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        return Map.of(
                "recordType", environment -> {
                    final CatalogRecord source = environment.getSource();
                    return CatalogRecordType.getByDomainClass(source);
                },
                "name", nameFetcher,
                "comment", commentFetcher
        );
    }
}
