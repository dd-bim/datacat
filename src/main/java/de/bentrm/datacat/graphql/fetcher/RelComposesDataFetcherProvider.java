package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.domain.XtdObject;
import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.domain.relationship.XtdRelComposes;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.PageInfo;
import de.bentrm.datacat.graphql.dto.*;
import de.bentrm.datacat.service.RelComposesService;
import de.bentrm.datacat.service.Specification;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class RelComposesDataFetcherProvider implements QueryDataFetcherProvider, RootDataFetcherProvider, MutationDataFetcherProvider {

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private RelComposesService relComposesService;

    @Override
    public Map<String, DataFetcher> getQueryDataFetchers() {
        return Map.ofEntries(
                Map.entry("composesRelations", getAll())
        );
    }

    public Map<String, DataFetcher> getRootDataFetchers() {
        return Map.ofEntries(
                Map.entry("composes", composes()),
                Map.entry("composedBy", composedBy())
        );
    }

    public Map<String, DataFetcher> getRelComposesDataFetchers() {
        return Map.ofEntries(
                Map.entry("relatedThings", relatedThings())
        );
    }

    @Override
    public Map<String, DataFetcher> getMutationDataFetchers() {
        return Map.ofEntries(
                Map.entry("createComposesRelation", add()),
                Map.entry("updateComposesRelation", update()),
                Map.entry("deleteComposesRelation", remove())
        );
    }

    public DataFetcher<XtdRelComposes> add() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            AssociationInput dto = objectMapper.convertValue(input, AssociationInput.class);
            return relComposesService.create(dto);
        };
    }

    public DataFetcher<XtdRelComposes> update() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            AssociationUpdateInput dto = objectMapper.convertValue(input, AssociationUpdateInput.class);
            return relComposesService.update(dto);
        };
    }

    public DataFetcher<Optional<XtdRelComposes>> remove() {
        return environment -> {
            String id = environment.getArgument("id");
            return relComposesService.delete(id);
        };
    }

    public DataFetcher<Connection<XtdRelComposes>> getAll() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            FilterInput filterInput = objectMapper.convertValue(input, FilterInput.class);
            if (filterInput == null) filterInput = new FilterInput();

            Specification spec = InputMapper.INSTANCE.toSpecification(filterInput);
            Page<XtdRelComposes> page = relComposesService.search(spec);
            return new Connection<>(page.getContent(), PageInfo.of(page), page.getTotalElements());
        };
    }

    public DataFetcher<Connection<XtdRelComposes>> composes() {
        return environment -> {
            XtdRoot root = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            PagingOptions dto = objectMapper.convertValue(input, PagingOptions.class);

            if (dto == null) dto = PagingOptions.defaults();

            List<String> ids = root.getComposes().stream().map(XtdRelComposes::getId).collect(Collectors.toList());
            Page<XtdRelComposes> page = relComposesService.findByIds(ids, dto.getPageble());

            return new Connection<>(page.getContent(), PageInfo.of(page), page.getTotalElements());
        };
    }

    public DataFetcher<Connection<XtdRelComposes>> composedBy() {
        return environment -> {
            XtdRoot source = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            PagingOptions dto = objectMapper.convertValue(input, PagingOptions.class);

            if (dto == null) dto = PagingOptions.defaults();

            List<String> ids = source.getComposedBy().stream().map(XtdRelComposes::getId).collect(Collectors.toList());
            Page<XtdRelComposes> page = relComposesService.findByIds(ids, dto.getPageble());

            return new Connection<>(page.getContent(), PageInfo.of(page), page.getTotalElements());
        };
    }

    public DataFetcher<Connection<XtdRoot>> relatedThings() {
        return environment -> {
            XtdRelComposes source = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            PagingOptions dto = objectMapper.convertValue(input, PagingOptions.class);

            if (dto == null) dto = PagingOptions.defaults();

            List<XtdRoot> content = new ArrayList<>(source.getRelatedThings());
            Page<XtdRoot> page = PageableExecutionUtils.getPage(content, dto.getPageble(), () -> source.getRelatedThings().size());
            return new Connection<>(page.getContent(), PageInfo.of(page), page.getTotalElements());
        };
    }

    public DataFetcher<Connection<XtdRelComposes>> getByRelatingObjectId() {
        return environment -> {
            XtdRoot source = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            PagingOptions dto = objectMapper.convertValue(input, PagingOptions.class);
            if (dto == null) dto = PagingOptions.defaults();

            Page<XtdRelComposes> page = relComposesService.findByRelatingThingId(source.getId(), dto.getPageble());
            return new Connection<>(page.getContent(), PageInfo.of(page), page.getTotalElements());
        };
    }

    public DataFetcher<Connection<XtdRelComposes>> getByRelatedObjectId() {
        return environment -> {
            XtdObject source = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            PagingOptions dto = objectMapper.convertValue(input, PagingOptions.class);
            if (dto == null) dto = PagingOptions.defaults();

            Page<XtdRelComposes> page = relComposesService.findByRelatedThingId(source.getId(), dto.getPageble());
            return new Connection<>(page.getContent(), PageInfo.of(page), page.getTotalElements());
        };
    }
}
