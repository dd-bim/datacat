package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.domain.Entity;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.FilterInput;
import de.bentrm.datacat.graphql.dto.InputMapper;
import de.bentrm.datacat.service.CrudEntityService;
import de.bentrm.datacat.specification.CatalogItemSpecification;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;
import java.util.Map;

public class GetAllFetcher<T extends Entity, S extends CrudEntityService<T, ?, ?>> implements DataFetcher<Connection<T>> {

    private final S entityService;
    ObjectMapper objectMapper = new ObjectMapper();

    public GetAllFetcher(@NotNull S entityService) {
        this.entityService = entityService;
    }

    @Override
    public Connection<T> get(DataFetchingEnvironment environment) throws Exception {
        Map<String, Object> input = environment.getArgument("input");
        FilterInput filterInput = objectMapper.convertValue(input, FilterInput.class);
        if (filterInput == null) filterInput = new FilterInput();

        CatalogItemSpecification specification = InputMapper.INSTANCE.toCatalogItemSpecification(filterInput);
        if (environment.getSelectionSet().containsAnyOf("nodes/*", "pageInfo/*")) {
            Page<T> page = entityService.findAll(specification);
            return Connection.of(page);
        } else {
            long count = entityService.count(specification);
            return Connection.empty(count);
        }
    }
}
