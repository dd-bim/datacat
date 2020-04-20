package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.domain.XtdObject;
import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.domain.relationship.XtdRelAssociates;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.AssociationInput;
import de.bentrm.datacat.graphql.dto.AssociationUpdateInput;
import de.bentrm.datacat.graphql.dto.PagingOptions;
import de.bentrm.datacat.service.RelAssociatesService;
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
public class RelAssociatesDataFetcherProvider implements EntityDataFetcherProvider<XtdRelAssociates> {

    @Autowired
    private RelAssociatesService relAssociatesService;

    @Override
    public Map<String, DataFetcher> getQueryDataFetchers() {
        return Map.ofEntries(
                Map.entry("associatesRelation", getOne()),
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

    @Override
    public DataFetcher<XtdRelAssociates> add() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            ObjectMapper mapper = new ObjectMapper();
            AssociationInput dto = mapper.convertValue(input, AssociationInput.class);
            return relAssociatesService.create(dto);
        };
    }

    @Override
    public DataFetcher<XtdRelAssociates> update() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            ObjectMapper mapper = new ObjectMapper();
            AssociationUpdateInput dto = mapper.convertValue(input, AssociationUpdateInput.class);
            return relAssociatesService.update(dto);
        };
    }

    @Override
    public DataFetcher<Optional<XtdRelAssociates>> remove() {
        return environment -> {
            String id = environment.getArgument("id");
            return relAssociatesService.delete(id);
        };
    }

    @Override
    public DataFetcher<Optional<XtdRelAssociates>> getOne() {
        return environment -> {
            String id = environment.getArgument("id");
            return relAssociatesService.findById(id);
        };
    }

    @Override
    public DataFetcher<Connection<XtdRelAssociates>> getAll() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            PagingOptions dto = mapper.convertValue(input, PagingOptions.class);
            if (dto == null) dto = PagingOptions.defaults();

            Page<XtdRelAssociates> page;
            String term = environment.getArgument("term");
            if (term != null && !term.isBlank()) {
                page = relAssociatesService.findByTerm(term.trim(), dto.getPageble());
            } else {
                page = relAssociatesService.findAll(dto.getPageble());
            }

            return new Connection<>(page);
        };
    }

    public DataFetcher<Connection<XtdRelAssociates>> associates() {
        return environment -> {
            XtdRoot root = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            PagingOptions dto = mapper.convertValue(input, PagingOptions.class);

            if (dto == null) dto = PagingOptions.defaults();

            List<String> ids = root.getAssociates().stream().map(XtdRelAssociates::getId).collect(Collectors.toList());
            Page<XtdRelAssociates> page = relAssociatesService.findByIds(ids, dto.getPageble());

            return new Connection<>(page);
        };
    }

    public DataFetcher<Connection<XtdRelAssociates>> associatedBy() {
        return environment -> {
            XtdRoot source = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            PagingOptions dto = mapper.convertValue(input, PagingOptions.class);

            if (dto == null) dto = PagingOptions.defaults();

            List<String> ids = source.getAssociatedBy().stream().map(XtdRelAssociates::getId).collect(Collectors.toList());
            Page<XtdRelAssociates> page = relAssociatesService.findByIds(ids, dto.getPageble());

            return new Connection<>(page);
        };
    }

    public DataFetcher<Connection<XtdRoot>> relatedThings() {
        return environment -> {
            XtdRelAssociates source = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            PagingOptions dto = mapper.convertValue(input, PagingOptions.class);

            if (dto == null) dto = PagingOptions.defaults();

            List<XtdRoot> content = new ArrayList<>(source.getRelatedThings());
            Page<XtdRoot> page = PageableExecutionUtils.getPage(content, dto.getPageble(), () -> source.getRelatedThings().size());
            return new Connection<>(page);
        };
    }

    public DataFetcher<Connection<XtdRelAssociates>> getByRelatingObjectId() {
        return environment -> {
            XtdRoot source = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            PagingOptions dto = mapper.convertValue(input, PagingOptions.class);
            if (dto == null) dto = PagingOptions.defaults();

            Page<XtdRelAssociates> page = relAssociatesService.findByRelatingThingId(source.getId(), dto.getPageble());
            return new Connection<>(page);
        };
    }

    public DataFetcher<Connection<XtdRelAssociates>> getByRelatedObjectId() {
        return environment -> {
            XtdObject source = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            PagingOptions dto = mapper.convertValue(input, PagingOptions.class);
            if (dto == null) dto = PagingOptions.defaults();

            Page<XtdRelAssociates> page = relAssociatesService.findByRelatedThingId(source.getId(), dto.getPageble());
            return new Connection<>(page);
        };
    }
}