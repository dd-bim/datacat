package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.domain.XtdObject;
import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.domain.relationship.XtdRelGroups;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.AssociationInput;
import de.bentrm.datacat.graphql.dto.AssociationUpdateInput;
import de.bentrm.datacat.graphql.dto.PagingOptions;
import de.bentrm.datacat.service.RelGroupsService;
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
public class RelGroupsDataFetcherProvider implements EntityDataFetcherProvider<XtdRelGroups> {

    @Autowired
    private RelGroupsService relGroupsService;

    @Override
    public Map<String, DataFetcher> getQueryDataFetchers() {
        return Map.ofEntries(
                Map.entry("groupsRelation", getOne()),
                Map.entry("groupsRelations", getAll())
        );
    }

    public Map<String, DataFetcher> getRootDataFetchers() {
        return Map.ofEntries(
                Map.entry("groups", groups()),
                Map.entry("groupedBy", groupedBy())
        );
    }

    public Map<String, DataFetcher> getRelGroupsDataFetchers() {
        return Map.ofEntries(
                Map.entry("relatedThings", relatedThings())
        );
    }

    @Override
    public Map<String, DataFetcher> getMutationDataFetchers() {
        return Map.ofEntries(
                Map.entry("createGroupsRelation", add()),
                Map.entry("updateGroupsRelation", update()),
                Map.entry("deleteGroupsRelation", remove())
        );
    }

    @Override
    public DataFetcher<XtdRelGroups> add() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            ObjectMapper mapper = new ObjectMapper();
            AssociationInput dto = mapper.convertValue(input, AssociationInput.class);
            return relGroupsService.create(dto);
        };
    }

    @Override
    public DataFetcher<XtdRelGroups> update() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            ObjectMapper mapper = new ObjectMapper();
            AssociationUpdateInput dto = mapper.convertValue(input, AssociationUpdateInput.class);
            return relGroupsService.update(dto);
        };
    }

    @Override
    public DataFetcher<Optional<XtdRelGroups>> remove() {
        return environment -> {
            String id = environment.getArgument("id");
            return relGroupsService.delete(id);
        };
    }

    @Override
    public DataFetcher<Optional<XtdRelGroups>> getOne() {
        return environment -> {
            String id = environment.getArgument("id");
            return relGroupsService.findById(id);
        };
    }

    @Override
    public DataFetcher<Connection<XtdRelGroups>> getAll() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            PagingOptions dto = mapper.convertValue(input, PagingOptions.class);
            if (dto == null) dto = PagingOptions.defaults();

            Page<XtdRelGroups> page;
            String term = environment.getArgument("term");
            if (term != null && !term.isBlank()) {
                page = relGroupsService.findByTerm(term.trim(), dto.getPageble());
            } else {
                page = relGroupsService.findAll(dto.getPageble());
            }

            return new Connection<>(page);
        };
    }

    public DataFetcher<Connection<XtdRelGroups>> groups() {
        return environment -> {
            XtdRoot root = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            PagingOptions dto = mapper.convertValue(input, PagingOptions.class);

            if (dto == null) dto = PagingOptions.defaults();

            List<String> ids = root.getGroups().stream().map(XtdRelGroups::getId).collect(Collectors.toList());
            Page<XtdRelGroups> page = relGroupsService.findByIds(ids, dto.getPageble());

            return new Connection<>(page);
        };
    }

    public DataFetcher<Connection<XtdRelGroups>> groupedBy() {
        return environment -> {
            XtdRoot source = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            PagingOptions dto = mapper.convertValue(input, PagingOptions.class);

            if (dto == null) dto = PagingOptions.defaults();

            List<String> ids = source.getGroupedBy().stream().map(XtdRelGroups::getId).collect(Collectors.toList());
            Page<XtdRelGroups> page = relGroupsService.findByIds(ids, dto.getPageble());

            return new Connection<>(page);
        };
    }

    public DataFetcher<Connection<XtdRoot>> relatedThings() {
        return environment -> {
            XtdRelGroups source = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            PagingOptions dto = mapper.convertValue(input, PagingOptions.class);

            if (dto == null) dto = PagingOptions.defaults();

            List<XtdRoot> content = new ArrayList<>(source.getRelatedThings());
            Page<XtdRoot> page = PageableExecutionUtils.getPage(content, dto.getPageble(), () -> source.getRelatedThings().size());
            return new Connection<>(page);
        };
    }

    public DataFetcher<Connection<XtdRelGroups>> getByRelatingObjectId() {
        return environment -> {
            XtdRoot source = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            PagingOptions dto = mapper.convertValue(input, PagingOptions.class);
            if (dto == null) dto = PagingOptions.defaults();

            Page<XtdRelGroups> page = relGroupsService.findByRelatingObjectId(source.getId(), dto.getPageble());
            return new Connection<>(page);
        };
    }

    public DataFetcher<Connection<XtdRelGroups>> getByRelatedObjectId() {
        return environment -> {
            XtdObject source = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            PagingOptions dto = mapper.convertValue(input, PagingOptions.class);
            if (dto == null) dto = PagingOptions.defaults();

            Page<XtdRelGroups> page = relGroupsService.findByRelatedObjectId(source.getId(), dto.getPageble());
            return new Connection<>(page);
        };
    }
}
