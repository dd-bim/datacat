package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.domain.Entity;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.PageInfo;
import de.bentrm.datacat.graphql.dto.DtoMapper;
import de.bentrm.datacat.graphql.dto.FilterInput;
import de.bentrm.datacat.service.CrudEntityService;
import de.bentrm.datacat.service.Specification;
import graphql.schema.DataFetcher;
import org.springframework.data.domain.Page;

import java.util.Map;
import java.util.Optional;

public abstract class EntityDataFetcherProviderImpl<
        T extends Entity, C, U,
        S extends CrudEntityService<T, C, U>
        > implements DataFetcherProvider {

    private final Class<C> inputClass;
    private final Class<U> updateClass;
    private final S entityService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public EntityDataFetcherProviderImpl(Class<C> inputClass, Class<U> updateClass, S entityService) {
        this.inputClass = inputClass;
        this.updateClass = updateClass;
        this.entityService = entityService;
    }

    public DataFetcher<T> add() {
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


    public DataFetcher<Optional<T>> remove() {
        return environment -> {
            String id = environment.getArgument("id");
            return entityService.delete(id);
        };
    }

    public DataFetcher<Connection<T>> getAll() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            FilterInput filterInput = objectMapper.convertValue(input, FilterInput.class);
            if (filterInput == null) filterInput = new FilterInput();

            Specification spec = DtoMapper.INSTANCE.toSpecification(filterInput);
            Page<T> page = entityService.search(spec);
            return new Connection<>(page.getContent(), PageInfo.of(page), page.getTotalElements());
        };
    }

}
