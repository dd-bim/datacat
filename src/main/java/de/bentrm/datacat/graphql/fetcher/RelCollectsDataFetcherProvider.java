package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.domain.collection.XtdCollection;
import de.bentrm.datacat.domain.relationship.XtdRelCollects;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.PageInfo;
import de.bentrm.datacat.graphql.dto.*;
import de.bentrm.datacat.service.RelCollectsService;
import de.bentrm.datacat.service.Specification;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class RelCollectsDataFetcherProvider implements QueryDataFetcherProvider, RootDataFetcherProvider, MutationDataFetcherProvider {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private RelCollectsService relCollectsService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Map<String, DataFetcher> getQueryDataFetchers() {
        return Map.ofEntries(
                Map.entry("collectsRelations", getAll())
        );
    }

    public Map<String, DataFetcher> getRootDataFetchers() {
        return Map.ofEntries(
                Map.entry("collectedBy", collectedBy())
        );
    }

    @Override
    public Map<String, DataFetcher> getMutationDataFetchers() {
        return Map.ofEntries(
                Map.entry("createCollectsRelation", add()),
                Map.entry("updateCollectsRelation", update()),
                Map.entry("deleteCollectsRelation", remove())
        );
    }

    public DataFetcher<XtdRelCollects> add() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            CollectsInput dto = mapper.convertValue(input, CollectsInput.class);
            return relCollectsService.create(dto);
        };
    }

    public DataFetcher<XtdRelCollects> update() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            CollectsUpdateInput dto = mapper.convertValue(input, CollectsUpdateInput.class);
            return relCollectsService.update(dto);
        };
    }

    public DataFetcher<Optional<XtdRelCollects>> remove() {
        return environment -> {
            String id = environment.getArgument("id");
            return relCollectsService.delete(id);
        };
    }

    public DataFetcher<Connection<XtdRelCollects>> getAll() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            FilterInput filterInput = objectMapper.convertValue(input, FilterInput.class);
            if (filterInput == null) filterInput = new FilterInput();

            Specification spec = InputMapper.INSTANCE.toSpecification(filterInput);
            Page<XtdRelCollects> page = relCollectsService.search(spec);
            return new Connection<>(page.getContent(), PageInfo.of(page), page.getTotalElements());
        };
    }

    public DataFetcher<Connection<XtdRelCollects>> collects() {
        return environment -> {
            XtdCollection collection = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            PagingOptions dto = mapper.convertValue(input, PagingOptions.class);

            if (dto == null) dto = PagingOptions.defaults();

            List<String> ids = collection.getCollects().stream().map(XtdRelCollects::getId).collect(Collectors.toList());
            Page<XtdRelCollects> page = relCollectsService.findByIds(ids, dto.getPageble());

            return new Connection<>(page.getContent(), PageInfo.of(page), page.getTotalElements());
        };
    }

    public DataFetcher<Connection<XtdRelCollects>> collectedBy() {
        return environment -> {
            XtdRoot source = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            PagingOptions dto = mapper.convertValue(input, PagingOptions.class);

            if (dto == null) dto = PagingOptions.defaults();

            List<String> ids = source.getCollectedBy().stream().map(XtdRelCollects::getId).collect(Collectors.toList());
            Page<XtdRelCollects> page = relCollectsService.findByIds(ids, dto.getPageble());

            return new Connection<>(page.getContent(), PageInfo.of(page), page.getTotalElements());
        };
    }
}
