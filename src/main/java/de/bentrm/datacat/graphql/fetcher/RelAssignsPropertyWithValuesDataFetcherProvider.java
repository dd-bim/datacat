package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.domain.relationship.XtdRelAssignsPropertyWithValues;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.PageInfo;
import de.bentrm.datacat.graphql.dto.AssignsPropertyWithValuesInput;
import de.bentrm.datacat.graphql.dto.AssignsPropertyWithValuesUpdateInput;
import de.bentrm.datacat.graphql.dto.DtoMapper;
import de.bentrm.datacat.graphql.dto.FilterInput;
import de.bentrm.datacat.service.RelAssignsPropertyWithValuesService;
import de.bentrm.datacat.service.Specification;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class RelAssignsPropertyWithValuesDataFetcherProvider implements QueryDataFetcherProvider, MutationDataFetcherProvider {

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private RelAssignsPropertyWithValuesService relAssignsPropertyWithValuesService;

    @Override
    public Map<String, DataFetcher> getQueryDataFetchers() {
        return Map.ofEntries(
                Map.entry("assignsPropertyWithValues", getAll())
        );
    }

    @Override
    public Map<String, DataFetcher> getMutationDataFetchers() {
        return Map.ofEntries(
                Map.entry("createAssignsPropertyWithValues", add()),
                Map.entry("updateAssignsPropertyWithValues", update()),
                Map.entry("deleteAssignsPropertyWithValues", remove())
        );
    }

    public DataFetcher<XtdRelAssignsPropertyWithValues> add() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            AssignsPropertyWithValuesInput dto = objectMapper.convertValue(input, AssignsPropertyWithValuesInput.class);
            return relAssignsPropertyWithValuesService.create(dto);
        };
    }

    public DataFetcher<XtdRelAssignsPropertyWithValues> update() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            AssignsPropertyWithValuesUpdateInput dto = objectMapper.convertValue(input, AssignsPropertyWithValuesUpdateInput.class);
            return relAssignsPropertyWithValuesService.update(dto);
        };
    }

    public DataFetcher<Optional<XtdRelAssignsPropertyWithValues>> remove() {
        return environment -> {
            String id = environment.getArgument("id");
            return relAssignsPropertyWithValuesService.delete(id);
        };
    }

    public DataFetcher<Connection<XtdRelAssignsPropertyWithValues>> getAll() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            FilterInput filterInput = objectMapper.convertValue(input, FilterInput.class);
            if (filterInput == null) filterInput = new FilterInput();

            Specification spec = DtoMapper.INSTANCE.toSpecification(filterInput);
            Page<XtdRelAssignsPropertyWithValues> page = relAssignsPropertyWithValuesService.search(spec);
            return new Connection<>(page.getContent(), PageInfo.of(page), page.getTotalElements());
        };
    }
}
