package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.domain.XtdObject;
import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.domain.relationship.XtdRelComposes;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.AssociationInput;
import de.bentrm.datacat.graphql.dto.AssociationUpdateInput;
import de.bentrm.datacat.graphql.dto.PagingOptions;
import de.bentrm.datacat.service.RelComposesService;
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
public class RelComposesDataFetcherProvider implements EntityDataFetcherProvider<XtdRelComposes> {

    @Autowired
    private RelComposesService relComposesService;

    @Override
    public Map<String, DataFetcher> getQueryDataFetchers() {
        return Map.ofEntries(
                Map.entry("composesRelation", getOne()),
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

    @Override
    public DataFetcher<XtdRelComposes> add() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            ObjectMapper mapper = new ObjectMapper();
            AssociationInput dto = mapper.convertValue(input, AssociationInput.class);
            return relComposesService.create(dto);
        };
    }

    @Override
    public DataFetcher<XtdRelComposes> update() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            ObjectMapper mapper = new ObjectMapper();
            AssociationUpdateInput dto = mapper.convertValue(input, AssociationUpdateInput.class);
            return relComposesService.update(dto);
        };
    }

    @Override
    public DataFetcher<Optional<XtdRelComposes>> remove() {
        return environment -> {
            String id = environment.getArgument("id");
            return relComposesService.delete(id);
        };
    }

    @Override
    public DataFetcher<Optional<XtdRelComposes>> getOne() {
        return environment -> {
            String id = environment.getArgument("id");
            return relComposesService.findById(id);
        };
    }

    @Override
    public DataFetcher<Connection<XtdRelComposes>> getAll() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            PagingOptions dto = mapper.convertValue(input, PagingOptions.class);
            if (dto == null) dto = PagingOptions.defaults();

            Page<XtdRelComposes> page;
            String term = environment.getArgument("term");
            if (term != null && !term.isBlank()) {
                page = relComposesService.findByTerm(term.trim(), dto.getPageble());
            } else {
                page = relComposesService.findAll(dto.getPageble());
            }

            return new Connection<>(page);
        };
    }

    public DataFetcher<Connection<XtdRelComposes>> composes() {
        return environment -> {
            XtdRoot root = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            PagingOptions dto = mapper.convertValue(input, PagingOptions.class);

            if (dto == null) dto = PagingOptions.defaults();

            List<String> ids = root.getComposes().stream().map(XtdRelComposes::getId).collect(Collectors.toList());
            Page<XtdRelComposes> page = relComposesService.findByIds(ids, dto.getPageble());

            return new Connection<>(page);
        };
    }

    public DataFetcher<Connection<XtdRelComposes>> composedBy() {
        return environment -> {
            XtdRoot source = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            PagingOptions dto = mapper.convertValue(input, PagingOptions.class);

            if (dto == null) dto = PagingOptions.defaults();

            List<String> ids = source.getComposedBy().stream().map(XtdRelComposes::getId).collect(Collectors.toList());
            Page<XtdRelComposes> page = relComposesService.findByIds(ids, dto.getPageble());

            return new Connection<>(page);
        };
    }

    public DataFetcher<Connection<XtdRoot>> relatedThings() {
        return environment -> {
            XtdRelComposes source = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            PagingOptions dto = mapper.convertValue(input, PagingOptions.class);

            if (dto == null) dto = PagingOptions.defaults();

            List<XtdRoot> content = new ArrayList<>(source.getRelatedThings());
            Page<XtdRoot> page = PageableExecutionUtils.getPage(content, dto.getPageble(), () -> source.getRelatedThings().size());
            return new Connection<>(page);
        };
    }

    public DataFetcher<Connection<XtdRelComposes>> getByRelatingObjectId() {
        return environment -> {
            XtdRoot source = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            PagingOptions dto = mapper.convertValue(input, PagingOptions.class);
            if (dto == null) dto = PagingOptions.defaults();

            Page<XtdRelComposes> page = relComposesService.findByRelatingThingId(source.getId(), dto.getPageble());
            return new Connection<>(page);
        };
    }

    public DataFetcher<Connection<XtdRelComposes>> getByRelatedObjectId() {
        return environment -> {
            XtdObject source = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            PagingOptions dto = mapper.convertValue(input, PagingOptions.class);
            if (dto == null) dto = PagingOptions.defaults();

            Page<XtdRelComposes> page = relComposesService.findByRelatedThingId(source.getId(), dto.getPageble());
            return new Connection<>(page);
        };
    }
}
