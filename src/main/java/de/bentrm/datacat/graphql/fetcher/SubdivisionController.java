package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdSubdivision;
import de.bentrm.datacat.catalog.service.SubdivisionRecordService;
import de.bentrm.datacat.catalog.specification.CatalogRecordSpecification;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.FilterInput;
import de.bentrm.datacat.graphql.dto.SpecificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class SubdivisionController {

    @Autowired
    private SubdivisionRecordService service;

    @Autowired
    private SpecificationMapper specificationMapper;

    @QueryMapping
    public Optional<XtdSubdivision> getSubdivision(@Argument String id) {
        return service.findByIdWithDirectRelations(id, XtdSubdivision.class.getSimpleName());
    }
    
    @QueryMapping
    public Connection<XtdSubdivision> findSubdivisions(@Argument FilterInput input) {
        if (input == null) input = new FilterInput();
        final CatalogRecordSpecification specification = specificationMapper.toCatalogRecordSpecification(input);
        final Page<XtdSubdivision> page = service.findAll(specification);
        return Connection.of(page);
    }

    @BatchMapping(typeName = "XtdSubdivision", field = "subdivisions")
    public Map<XtdSubdivision, List<XtdSubdivision>> getSubdivisions(List<XtdSubdivision> subdivisions) {
        return subdivisions.stream()
                .filter(subdivision -> subdivision != null)  // Filter out null subdivisions
                .collect(Collectors.toMap(
                        subdivision -> subdivision,
                        subdivision -> {
                            List<XtdSubdivision> result = service.getSubdivisions(subdivision);
                            return result != null ? result : new ArrayList<>();  // Handle null result
                        }
                ));                
    }
}
