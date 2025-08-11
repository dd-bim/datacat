package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdQuantityKind;
import de.bentrm.datacat.catalog.domain.XtdUnit;
import de.bentrm.datacat.catalog.domain.XtdDimension;
import de.bentrm.datacat.catalog.service.QuantityKindRecordService;
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

import java.util.List;
import java.util.Optional;

@Controller
public class QuantityKindController {

    @Autowired
    private QuantityKindRecordService service;

    @Autowired
    private SpecificationMapper specificationMapper;

    @QueryMapping
    public Optional<XtdQuantityKind> getQuantityKind(@Argument String id) {
        return service.findByIdWithDirectRelations(id, XtdQuantityKind.class.getSimpleName());
    }

    @QueryMapping
    public Connection<XtdQuantityKind> findQuantityKinds(@Argument FilterInput input) {
        if (input == null)
            input = new FilterInput();
        final CatalogRecordSpecification specification = specificationMapper.toCatalogRecordSpecification(input);
        final Page<XtdQuantityKind> page = service.findAll(specification);
        return Connection.of(page);
    }

    @SchemaMapping(typeName = "XtdQuantityKind", field = "units")
    public List<XtdUnit> getUnits(XtdQuantityKind quantityKind) {
        return service.getUnits(quantityKind);
    }

    @SchemaMapping(typeName = "XtdQuantityKind", field = "dimension")
    public Optional<XtdDimension> getDimension(XtdQuantityKind quantityKind) {
        return service.getDimension(quantityKind);
    }
}
