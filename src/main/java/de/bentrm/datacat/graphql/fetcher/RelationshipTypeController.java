package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdRelationshipType;
import de.bentrm.datacat.catalog.service.RelationshipTypeRecordService;
import de.bentrm.datacat.catalog.specification.CatalogRecordSpecification;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.FilterInput;
import de.bentrm.datacat.graphql.dto.SpecificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class RelationshipTypeController {

    @Autowired
    private RelationshipTypeRecordService service;

    @Autowired
    private SpecificationMapper specificationMapper;

    @QueryMapping
    public Optional<XtdRelationshipType> getRelationshipType(@Argument String id) {
        return service.findByIdWithDirectRelations(id, XtdRelationshipType.class.getSimpleName());
    }

    @QueryMapping
    public Connection<XtdRelationshipType> findRelationshipTypes(@Argument FilterInput input) {
        if (input == null)
            input = new FilterInput();
        final CatalogRecordSpecification specification = specificationMapper.toCatalogRecordSpecification(input);
        final Page<XtdRelationshipType> page = service.findAll(specification);
        return Connection.of(page);
    }
}
