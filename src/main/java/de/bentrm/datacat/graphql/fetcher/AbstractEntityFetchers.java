package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.domain.Entity;
import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.service.CatalogService;
import de.bentrm.datacat.service.CrudEntityService;
import de.bentrm.datacat.specification.RootSpecification;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public abstract class AbstractEntityFetchers<
        T extends Entity, C, U,
        S extends CrudEntityService<T, C, U>>
        implements AttributeFetchers, QueryFetchers, MutationFetchers {

    protected final Class<C> inputClass;
    protected final Class<U> updateClass;
    protected final S entityService;
    protected CatalogService catalogService;

    public AbstractEntityFetchers(Class<C> inputClass, Class<U> updateClass, S entityService) {
        this.inputClass = inputClass;
        this.updateClass = updateClass;
        this.entityService = entityService;
    }

    public abstract String getQueryName();

    public abstract String getMutationNameSuffix();

    @Autowired
    public void setCatalogService(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @Override
    public Map<String, DataFetcher> getQueryFetchers() {
        final String queryName = getQueryName();
        return Map.of(queryName, fetchAll());
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        return new HashMap<>();
    }

    @Override
    public Map<String, DataFetcher> getMutationFetchers() {
        final String suffix = getMutationNameSuffix();
        return Map.of(
                "create" + suffix, create(),
                "update" + suffix, update(),
                "delete" + suffix, delete()
        );
    }

    public DataFetcher<T> create() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            ObjectMapper mapper = new ObjectMapper();
            C dto = mapper.convertValue(input, inputClass);
            return entityService.create(dto);
        };
    }

    public DataFetcher<T> update() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            ObjectMapper mapper = new ObjectMapper();
            U dto = mapper.convertValue(input, updateClass);
            return entityService.update(dto);
        };
    }

    public DataFetcher<Optional<T>> delete() {
        return environment -> {
            String id = environment.getArgument("id");
            return entityService.delete(id);
        };
    }

    public DataFetcher<Connection<T>> fetchAll() {
        return new GetAllFetcher<>(entityService);
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
