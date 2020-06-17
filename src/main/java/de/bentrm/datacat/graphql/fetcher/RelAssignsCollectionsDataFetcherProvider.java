package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.domain.relationship.XtdRelAssignsCollections;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.PageInfo;
import de.bentrm.datacat.graphql.dto.AssignsCollectionsInput;
import de.bentrm.datacat.graphql.dto.AssignsCollectionsUpdateInput;
import de.bentrm.datacat.graphql.dto.DtoMapper;
import de.bentrm.datacat.graphql.dto.FilterInput;
import de.bentrm.datacat.service.RelAssignsCollectionsService;
import de.bentrm.datacat.service.Specification;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class RelAssignsCollectionsDataFetcherProvider implements QueryDataFetcherProvider, MutationDataFetcherProvider {

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private RelAssignsCollectionsService relAssignsCollectionsService;

    @Override
    public Map<String, DataFetcher> getQueryDataFetchers() {
        return Map.ofEntries(
                Map.entry("assignsCollectionsRelations", getAll())
        );
    }

    @Override
    public Map<String, DataFetcher> getMutationDataFetchers() {
        return Map.ofEntries(
                Map.entry("createAssignsCollectionsRelation", add()),
                Map.entry("updateAssignsCollectionsRelation", update()),
                Map.entry("deleteAssignsCollectionsRelation", remove())
        );
    }

    public DataFetcher<XtdRelAssignsCollections> add() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            AssignsCollectionsInput dto = objectMapper.convertValue(input, AssignsCollectionsInput.class);
            return relAssignsCollectionsService.create(dto);
        };
    }

    public DataFetcher<XtdRelAssignsCollections> update() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            AssignsCollectionsUpdateInput dto = objectMapper.convertValue(input, AssignsCollectionsUpdateInput.class);
            return relAssignsCollectionsService.update(dto);
        };
    }

    public DataFetcher<Optional<XtdRelAssignsCollections>> remove() {
        return environment -> {
            String id = environment.getArgument("id");
            return relAssignsCollectionsService.delete(id);
        };
    }

    public DataFetcher<Connection<XtdRelAssignsCollections>> getAll() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            FilterInput filterInput = objectMapper.convertValue(input, FilterInput.class);
            if (filterInput == null) filterInput = new FilterInput();

            Specification spec = DtoMapper.INSTANCE.toSpecification(filterInput);
            Page<XtdRelAssignsCollections> page = relAssignsCollectionsService.search(spec);
            return new Connection<>(page.getContent(), PageInfo.of(page), page.getTotalElements());
        };
    }
}
