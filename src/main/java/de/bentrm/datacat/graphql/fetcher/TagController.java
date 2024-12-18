package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.Tag;
import de.bentrm.datacat.catalog.service.TagService;
import de.bentrm.datacat.catalog.repository.TagRepository;
import de.bentrm.datacat.catalog.specification.TagSpecification;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.FilterInput;
import de.bentrm.datacat.graphql.dto.SpecificationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Slf4j
@Controller
public class TagController {

    @Autowired
    private SpecificationMapper specificationMapper;

    @Autowired
    private TagService tagService;

    @Autowired
    private TagRepository repository;


    @QueryMapping
    public Optional<Tag> getTag(@Argument String id) {
        return repository.findByIdWithDirectRelations(id);
    }

    @QueryMapping
    public Connection<Tag> findTags(@Argument FilterInput input) {
        if (input == null) input = new FilterInput();
        final TagSpecification specification = specificationMapper.toTagSpec(input);
        final Page<Tag> page = tagService.findAll(specification);
        return Connection.of(page);
    }
}
