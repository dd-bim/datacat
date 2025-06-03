package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdDimension;
import de.bentrm.datacat.catalog.domain.XtdMultiLanguageText;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdRational;
import de.bentrm.datacat.catalog.domain.XtdUnit;
import de.bentrm.datacat.catalog.domain.XtdValueList;
import de.bentrm.datacat.catalog.service.UnitRecordService;
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
public class UnitController {

    @Autowired
    private UnitRecordService service;

    @Autowired
    private SpecificationMapper specificationMapper;

    @QueryMapping
    public Optional<XtdUnit> getUnit(@Argument String id) {
        return service.findByIdWithDirectRelations(id, XtdUnit.class.getSimpleName());

    }
    
    @QueryMapping
    public Connection<XtdUnit> findUnits(@Argument FilterInput input) {
        if (input == null) input = new FilterInput();
        final CatalogRecordSpecification specification = specificationMapper.toCatalogRecordSpecification(input);
        final Page<XtdUnit> page = service.findAll(specification);
        return Connection.of(page);
    }

    @SchemaMapping(typeName = "XtdUnit", field = "properties")
    public List<XtdProperty> getProperties(XtdUnit unit) {
        return service.getProperties(unit);
    }

    @SchemaMapping(typeName = "XtdUnit", field = "dimension")
    public Optional<XtdDimension> getDimension(XtdUnit unit) {
        return service.getDimension(unit);
    }

    @SchemaMapping(typeName = "XtdUnit", field = "symbol")
    public Optional<XtdMultiLanguageText> getSymbol(XtdUnit unit) {
        return service.getSymbol(unit);
    }

    @SchemaMapping(typeName = "XtdUnit", field = "coefficient")
    public Optional<XtdRational> getCoefficient(XtdUnit unit) {
        return service.getCoefficient(unit);
    }

    @SchemaMapping(typeName = "XtdUnit", field = "offset")
    public Optional<XtdRational> getOffset(XtdUnit unit) {
        return service.getOffset(unit);
    }

    @SchemaMapping(typeName = "XtdUnit", field = "valueLists")
    public Optional<List<XtdValueList>> getValueLists(XtdUnit unit) {
        return service.getValueLists(unit);
    }
}
