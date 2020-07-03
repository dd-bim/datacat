package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.domain.XtdObject;
import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.domain.relationship.XtdRelActsUpon;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.PageInfo;
import de.bentrm.datacat.graphql.dto.*;
import de.bentrm.datacat.service.RelActsUponService;
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
public class RelActsUponDataFetcherProvider implements QueryDataFetcherProvider, RootDataFetcherProvider, MutationDataFetcherProvider {

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private RelActsUponService relActsUponService;

    @Override
    public Map<String, DataFetcher> getQueryDataFetchers() {
        return Map.ofEntries(
                Map.entry("actsUponRelations", getAll())
        );
    }

    public Map<String, DataFetcher> getRootDataFetchers() {
        return Map.ofEntries(
                Map.entry("actsUpon", actsUpon()),
                Map.entry("actedUponBy", actedUponBy())
        );
    }

    public Map<String, DataFetcher> getRelActsUponDataFetchers() {
        return Map.ofEntries(
                Map.entry("relatedThings", relatedThings())
        );
    }

    @Override
    public Map<String, DataFetcher> getMutationDataFetchers() {
        return Map.ofEntries(
                Map.entry("createActsUponRelation", add()),
                Map.entry("updateActsUponRelation", update()),
                Map.entry("deleteActsUponRelation", remove())
        );
    }

    public DataFetcher<XtdRelActsUpon> add() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            AssociationInput dto = objectMapper.convertValue(input, AssociationInput.class);
            return relActsUponService.create(dto);
        };
    }

    public DataFetcher<XtdRelActsUpon> update() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            AssociationUpdateInput dto = objectMapper.convertValue(input, AssociationUpdateInput.class);
            return relActsUponService.update(dto);
        };
    }

    public DataFetcher<Optional<XtdRelActsUpon>> remove() {
        return environment -> {
            String id = environment.getArgument("id");
            return relActsUponService.delete(id);
        };
    }

    public DataFetcher<Connection<XtdRelActsUpon>> getAll() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            FilterInput filterInput = objectMapper.convertValue(input, FilterInput.class);
            if (filterInput == null) filterInput = new FilterInput();

            Specification spec = InputMapper.INSTANCE.toSpecification(filterInput);
            Page<XtdRelActsUpon> page = relActsUponService.search(spec);
            return new Connection<>(page.getContent(), PageInfo.of(page), page.getTotalElements());
        };
    }

    public DataFetcher<Connection<XtdRelActsUpon>> actsUpon() {
        return environment -> {
            XtdRoot root = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            PagingOptions dto = objectMapper.convertValue(input, PagingOptions.class);

            if (dto == null) dto = PagingOptions.defaults();

            List<String> ids = root.getActsUpon().stream().map(XtdRelActsUpon::getId).collect(Collectors.toList());
            Page<XtdRelActsUpon> page = relActsUponService.findByIds(ids, dto.getPageble());

            return new Connection<>(page.getContent(), PageInfo.of(page), page.getTotalElements());
        };
    }

    public DataFetcher<Connection<XtdRelActsUpon>> actedUponBy() {
        return environment -> {
            XtdRoot source = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            PagingOptions dto = objectMapper.convertValue(input, PagingOptions.class);

            if (dto == null) dto = PagingOptions.defaults();

            List<String> ids = source.getActedUponBy().stream().map(XtdRelActsUpon::getId).collect(Collectors.toList());
            Page<XtdRelActsUpon> page = relActsUponService.findByIds(ids, dto.getPageble());

            return new Connection<>(page.getContent(), PageInfo.of(page), page.getTotalElements());
        };
    }

    public DataFetcher<Connection<XtdRoot>> relatedThings() {
        return environment -> {
            XtdRelActsUpon source = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            PagingOptions dto = objectMapper.convertValue(input, PagingOptions.class);

            if (dto == null) dto = PagingOptions.defaults();

            List<XtdRoot> content = new ArrayList<>(source.getRelatedThings());
            Page<XtdRoot> page = PageableExecutionUtils.getPage(content, dto.getPageble(), () -> source.getRelatedThings().size());
            return new Connection<>(page.getContent(), PageInfo.of(page), page.getTotalElements());
        };
    }

    public DataFetcher<Connection<XtdRelActsUpon>> getByRelatingObjectId() {
        return environment -> {
            XtdRoot source = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            PagingOptions dto = objectMapper.convertValue(input, PagingOptions.class);
            if (dto == null) dto = PagingOptions.defaults();

            Page<XtdRelActsUpon> page = relActsUponService.findByRelatingThingId(source.getId(), dto.getPageble());
            return new Connection<>(page.getContent(), PageInfo.of(page), page.getTotalElements());
        };
    }

    public DataFetcher<Connection<XtdRelActsUpon>> getByRelatedObjectId() {
        return environment -> {
            XtdObject source = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            PagingOptions dto = objectMapper.convertValue(input, PagingOptions.class);
            if (dto == null) dto = PagingOptions.defaults();

            Page<XtdRelActsUpon> page = relActsUponService.findByRelatedThingId(source.getId(), dto.getPageble());
            return new Connection<>(page.getContent(), PageInfo.of(page), page.getTotalElements());
        };
    }
}
