package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.domain.XtdObject;
import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.domain.relationship.XtdRelSpecializes;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.PageInfo;
import de.bentrm.datacat.graphql.dto.*;
import de.bentrm.datacat.service.RelSpecializesService;
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
public class RelSpecializesDataFetcherProvider implements QueryDataFetcherProvider, RootDataFetcherProvider, MutationDataFetcherProvider {

    @Autowired
    private RelSpecializesService relSpecializesService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Map<String, DataFetcher> getQueryDataFetchers() {
        return Map.ofEntries(
                Map.entry("specializesRelations", getAll())
        );
    }

    public Map<String, DataFetcher> getRootDataFetchers() {
        return Map.ofEntries(
                Map.entry("specializes", specializes()),
                Map.entry("specializedBy", specializedBy())
        );
    }

    public Map<String, DataFetcher> getRelSpecializesDataFetchers() {
        return Map.ofEntries(
                Map.entry("relatedThings", relatedThings())
        );
    }

    @Override
    public Map<String, DataFetcher> getMutationDataFetchers() {
        return Map.ofEntries(
                Map.entry("createSpecializesRelation", add()),
                Map.entry("updateSpecializesRelation", update()),
                Map.entry("deleteSpecializesRelation", remove())
        );
    }

    public DataFetcher<XtdRelSpecializes> add() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            ObjectMapper mapper = new ObjectMapper();
            AssociationInput dto = mapper.convertValue(input, AssociationInput.class);
            return relSpecializesService.create(dto);
        };
    }

    public DataFetcher<XtdRelSpecializes> update() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            ObjectMapper mapper = new ObjectMapper();
            AssociationUpdateInput dto = mapper.convertValue(input, AssociationUpdateInput.class);
            return relSpecializesService.update(dto);
        };
    }

    public DataFetcher<Optional<XtdRelSpecializes>> remove() {
        return environment -> {
            String id = environment.getArgument("id");
            return relSpecializesService.delete(id);
        };
    }

    public DataFetcher<Connection<XtdRelSpecializes>> getAll() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            FilterInput filterInput = objectMapper.convertValue(input, FilterInput.class);
            if (filterInput == null) filterInput = new FilterInput();

            Specification spec = DtoMapper.INSTANCE.toSpecification(filterInput);
            Page<XtdRelSpecializes> page = relSpecializesService.search(spec);
            return new Connection<>(page.getContent(), PageInfo.of(page), page.getTotalElements());
        };
    }

    public DataFetcher<Connection<XtdRelSpecializes>> specializes() {
        return environment -> {
            XtdRoot root = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            PagingOptions dto = mapper.convertValue(input, PagingOptions.class);

            if (dto == null) dto = PagingOptions.defaults();

            List<String> ids = root.getSpecializes().stream().map(XtdRelSpecializes::getId).collect(Collectors.toList());
            Page<XtdRelSpecializes> page = relSpecializesService.findByIds(ids, dto.getPageble());

            return new Connection<>(page.getContent(), PageInfo.of(page), page.getTotalElements());
        };
    }

    public DataFetcher<Connection<XtdRelSpecializes>> specializedBy() {
        return environment -> {
            XtdRoot source = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            PagingOptions dto = mapper.convertValue(input, PagingOptions.class);

            if (dto == null) dto = PagingOptions.defaults();

            List<String> ids = source.getSpecializedBy().stream().map(XtdRelSpecializes::getId).collect(Collectors.toList());
            Page<XtdRelSpecializes> page = relSpecializesService.findByIds(ids, dto.getPageble());

            return new Connection<>(page.getContent(), PageInfo.of(page), page.getTotalElements());
        };
    }

    public DataFetcher<Connection<XtdRoot>> relatedThings() {
        return environment -> {
            XtdRelSpecializes source = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            PagingOptions dto = mapper.convertValue(input, PagingOptions.class);

            if (dto == null) dto = PagingOptions.defaults();

            List<XtdRoot> content = new ArrayList<>(source.getRelatedThings());
            Page<XtdRoot> page = PageableExecutionUtils.getPage(content, dto.getPageble(), () -> source.getRelatedThings().size());
            return new Connection<>(page.getContent(), PageInfo.of(page), page.getTotalElements());
        };
    }

    public DataFetcher<Connection<XtdRelSpecializes>> getByRelatingObjectId() {
        return environment -> {
            XtdRoot source = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            PagingOptions dto = mapper.convertValue(input, PagingOptions.class);
            if (dto == null) dto = PagingOptions.defaults();

            Page<XtdRelSpecializes> page = relSpecializesService.findByRelatingThingId(source.getId(), dto.getPageble());
            return new Connection<>(page.getContent(), PageInfo.of(page), page.getTotalElements());
        };
    }

    public DataFetcher<Connection<XtdRelSpecializes>> getByRelatedObjectId() {
        return environment -> {
            XtdObject source = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            PagingOptions dto = mapper.convertValue(input, PagingOptions.class);
            if (dto == null) dto = PagingOptions.defaults();

            Page<XtdRelSpecializes> page = relSpecializesService.findByRelatedThingId(source.getId(), dto.getPageble());
            return new Connection<>(page.getContent(), PageInfo.of(page), page.getTotalElements());
        };
    }
}
