package de.bentrm.datacat.graphql.fetchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.catalog.domain.CatalogItem;
import de.bentrm.datacat.catalog.service.CatalogService;
import de.bentrm.datacat.catalog.specification.TagSpecification;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.FilterInput;
import de.bentrm.datacat.graphql.dto.SpecificationMapper;
import de.bentrm.datacat.graphql.dto.TagInput;
import de.bentrm.datacat.service.TagService;
import de.bentrm.datacat.service.dto.TagDto;
import graphql.schema.DataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class TagFetchers implements AttributeFetchers, QueryFetchers, MutationFetchers {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private SpecificationMapper specificationMapper;

    @Autowired
    private CatalogService catalogService;

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
                "tag", findById(),
                "tags", findAll()
        );
    }

    @Override
    public Map<String, DataFetcher> getMutationFetchers() {
        return Map.of(
                "createTag", create(),
                "updateTag", update(),
                "deleteTag", delete(),
                "tag", tag(),
                "untag", untag()
        );
    }

    private DataFetcher<TagDto> findById() {
        return environment -> {
            String id = environment.getArgument("id");
            return tagService.findById(id);
        };
    }

    private DataFetcher<Connection<TagDto>> findAll() {
        return environment -> {
            Map<String, Object> rawInput = environment.getArgument("input");
            FilterInput input = objectMapper.convertValue(rawInput, FilterInput.class);
            if (input == null) input = new FilterInput();
            final TagSpecification specification = specificationMapper.toTagSpec(input);
            final Page<TagDto> page = tagService.findAll(specification);
            return Connection.of(page);
        };
    }

    private DataFetcher<TagDto> create() {
        return environment -> {
            String id = environment.getArgument("id");
            Map<String, Object> rawInput = environment.getArgument("input");
            final TagInput input = objectMapper.convertValue(rawInput, TagInput.class);
            final TagDto tagDto = specificationMapper.toTagDto(id, input);
            return tagService.create(tagDto);
        };
    }

    private DataFetcher<TagDto> update() {
        return environment -> {
            String id = environment.getArgument("id");
            Map<String, Object> rawInput = environment.getArgument("input");
            final TagInput input = objectMapper.convertValue(rawInput, TagInput.class);
            final TagDto tagDto = specificationMapper.toTagDto(id, input);
            return tagService.update(tagDto);
        };
    }

    private DataFetcher<TagDto> delete() {
        return environment -> {
            String id = environment.getArgument("id");
            return tagService.delete(id);
        };
    }

    private DataFetcher<CatalogItem> tag() {
        return environment -> {
            String conceptId = environment.getArgument("conceptId");
            String tagId = environment.getArgument("tagId");
            return catalogService.tag(conceptId, tagId);
        };
    }

    private DataFetcher<CatalogItem> untag() {
        return environment -> {
            String conceptId = environment.getArgument("conceptId");
            String tagId = environment.getArgument("tagId");
            return catalogService.untag(conceptId, tagId);
        };
    }
}
