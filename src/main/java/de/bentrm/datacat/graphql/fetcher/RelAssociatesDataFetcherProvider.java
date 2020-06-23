package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.domain.XtdObject;
import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.domain.relationship.XtdRelAssociates;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.PageInfo;
import de.bentrm.datacat.graphql.dto.*;
import de.bentrm.datacat.service.RelAssociatesService;
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
public class RelAssociatesDataFetcherProvider implements QueryDataFetcherProvider, RootDataFetcherProvider, MutationDataFetcherProvider {

    @Autowired
    private RelAssociatesService relAssociatesService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Map<String, DataFetcher> getQueryDataFetchers() {
        return Map.ofEntries(
                Map.entry("associatesRelations", getAll())
        );
    }

    public Map<String, DataFetcher> getRootDataFetchers() {
        return Map.ofEntries(
                Map.entry("associates", associates()),
                Map.entry("associatedBy", associatedBy())
        );
    }

    public Map<String, DataFetcher> getRelAssociatesDataFetchers() {
        return Map.ofEntries(
                Map.entry("relatedThings", relatedThings())
        );
    }

    @Override
    public Map<String, DataFetcher> getMutationDataFetchers() {
        return Map.ofEntries(
                Map.entry("createAssociatesRelation", add()),
                Map.entry("updateAssociatesRelation", update()),
                Map.entry("deleteAssociatesRelation", remove())
        );
    }

    public DataFetcher<XtdRelAssociates> add() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            AssociationInput dto = objectMapper.convertValue(input, AssociationInput.class);
            return relAssociatesService.create(dto);
        };
    }

    public DataFetcher<XtdRelAssociates> update() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            AssociationUpdateInput dto = objectMapper.convertValue(input, AssociationUpdateInput.class);
            return relAssociatesService.update(dto);
        };
    }

    public DataFetcher<Optional<XtdRelAssociates>> remove() {
        return environment -> {
            String id = environment.getArgument("id");
            return relAssociatesService.delete(id);
        };
    }

    public DataFetcher<Connection<XtdRelAssociates>> getAll() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            FilterInput filterInput = objectMapper.convertValue(input, FilterInput.class);
            if (filterInput == null) filterInput = new FilterInput();

            Specification spec = DtoMapper.INSTANCE.toSpecification(filterInput);
            Page<XtdRelAssociates> page = relAssociatesService.search(spec);
            return new Connection<>(page.getContent(), PageInfo.of(page), page.getTotalElements());
        };
    }

    public DataFetcher<Connection<XtdRelAssociates>> associates() {
        return environment -> {
            XtdRoot root = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            PagingOptions dto = objectMapper.convertValue(input, PagingOptions.class);

            if (dto == null) dto = PagingOptions.defaults();

            List<String> ids = root.getAssociates().stream().map(XtdRelAssociates::getId).collect(Collectors.toList());
            Page<XtdRelAssociates> page = relAssociatesService.findByIds(ids, dto.getPageble());

            return new Connection<>(page.getContent(), PageInfo.of(page), page.getTotalElements());
        };
    }

    public DataFetcher<Connection<XtdRelAssociates>> associatedBy() {
        return environment -> {
            XtdRoot source = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            PagingOptions dto = objectMapper.convertValue(input, PagingOptions.class);

            if (dto == null) dto = PagingOptions.defaults();

            List<String> ids = source.getAssociatedBy().stream().map(XtdRelAssociates::getId).collect(Collectors.toList());
            Page<XtdRelAssociates> page = relAssociatesService.findByIds(ids, dto.getPageble());

            return new Connection<>(page.getContent(), PageInfo.of(page), page.getTotalElements());
        };
    }

    public DataFetcher<Connection<XtdRoot>> relatedThings() {
        return environment -> {
            XtdRelAssociates source = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            PagingOptions dto = objectMapper.convertValue(input, PagingOptions.class);

            if (dto == null) dto = PagingOptions.defaults();

            List<XtdRoot> content = new ArrayList<>(source.getRelatedThings());
            Page<XtdRoot> page = PageableExecutionUtils.getPage(content, dto.getPageble(), () -> source.getRelatedThings().size());
            return new Connection<>(page.getContent(), PageInfo.of(page), page.getTotalElements());
        };
    }

    public DataFetcher<Connection<XtdRelAssociates>> getByRelatingObjectId() {
        return environment -> {
            XtdRoot source = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            PagingOptions dto = objectMapper.convertValue(input, PagingOptions.class);
            if (dto == null) dto = PagingOptions.defaults();

            Page<XtdRelAssociates> page = relAssociatesService.findByRelatingThingId(source.getId(), dto.getPageble());
            return new Connection<>(page.getContent(), PageInfo.of(page), page.getTotalElements());
        };
    }

    public DataFetcher<Connection<XtdRelAssociates>> getByRelatedObjectId() {
        return environment -> {
            XtdObject source = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            PagingOptions dto = objectMapper.convertValue(input, PagingOptions.class);
            if (dto == null) dto = PagingOptions.defaults();

            Page<XtdRelAssociates> page = relAssociatesService.findByRelatedThingId(source.getId(), dto.getPageble());
            return new Connection<>(page.getContent(), PageInfo.of(page), page.getTotalElements());
        };
    }
}
