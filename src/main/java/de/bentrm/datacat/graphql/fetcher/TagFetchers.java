package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.catalog.domain.Tag;
import de.bentrm.datacat.catalog.service.TagService;
import de.bentrm.datacat.catalog.specification.TagSpecification;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.FilterInput;
import de.bentrm.datacat.graphql.dto.SpecificationMapper;
import graphql.schema.DataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class TagFetchers implements AttributeFetchers, QueryFetchers {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private SpecificationMapper specificationMapper;

    @Autowired
    private TagService tagService;

    @Override
    public String getTypeName() {
        return "Tag";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        return Map.of();
    }

    @Override
    public Map<String, DataFetcher> getQueryFetchers() {
        return Map.of(
                "getTag", findById(),
                "findTags", findAll()
        );
    }

    private DataFetcher<Optional<Tag>> findById() {
        return environment -> {
            String id = environment.getArgument("id");
            return tagService.findById(id);
        };
    }

    private DataFetcher<Connection<Tag>> findAll() {
        return environment -> {
            Map<String, Object> rawInput = environment.getArgument("input");
            FilterInput input = objectMapper.convertValue(rawInput, FilterInput.class);
            if (input == null) input = new FilterInput();
            final TagSpecification specification = specificationMapper.toTagSpec(input);
            final Page<Tag> page = tagService.findAll(specification);
            return Connection.of(page);
        };
    }
}
