package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdInterval;
import de.bentrm.datacat.catalog.domain.XtdValueList;
import de.bentrm.datacat.catalog.service.IntervalRecordService;
import de.bentrm.datacat.catalog.specification.CatalogRecordSpecification;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.FilterInput;
import de.bentrm.datacat.graphql.dto.SpecificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class IntervalController {

    @Autowired
    private IntervalRecordService service;

    @Autowired
    private SpecificationMapper specificationMapper;

    @QueryMapping
    public Optional<XtdInterval> getInterval(@Argument String id) {
        return service.findByIdWithDirectRelations(id, XtdInterval.class.getSimpleName());
    }

    @QueryMapping
    public Connection<XtdInterval> findIntervals(@Argument FilterInput input) {
        if (input == null)
            input = new FilterInput();
        final CatalogRecordSpecification specification = specificationMapper.toCatalogRecordSpecification(input);
        final Page<XtdInterval> page = service.findAll(specification);
        return Connection.of(page);
    }

    @SchemaMapping(typeName = "XtdInterval", field = "minimum")
    public Optional<XtdValueList> getMinimum(XtdInterval interval) {
        return service.getMinimum(interval);
    }

    @SchemaMapping(typeName = "XtdInterval", field = "maximum")
    public Optional<XtdValueList> getMaximum(XtdInterval interval) {
        return service.getMaximum(interval);
    }
}
