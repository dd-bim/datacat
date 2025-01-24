package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdDimension;
import de.bentrm.datacat.catalog.domain.XtdInterval;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdQuantityKind;
import de.bentrm.datacat.catalog.domain.XtdRelationshipToProperty;
import de.bentrm.datacat.catalog.domain.XtdSubject;
import de.bentrm.datacat.catalog.domain.XtdSymbol;
import de.bentrm.datacat.catalog.domain.XtdValueList;
import de.bentrm.datacat.catalog.domain.XtdUnit;
import de.bentrm.datacat.catalog.service.PropertyRecordService;
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
import java.util.List;

@Controller
public class PropertyController {

    @Autowired
    private PropertyRecordService service;

    @Autowired
    private SpecificationMapper specificationMapper;

    @QueryMapping
    public Optional<XtdProperty> getProperty(@Argument String id) {
        return service.findByIdWithDirectRelations(id, XtdProperty.class.getSimpleName());
    }

    @QueryMapping
    public Connection<XtdProperty> findProperties(@Argument FilterInput input) {
        if (input == null)
            input = new FilterInput();
        final CatalogRecordSpecification specification = specificationMapper.toCatalogRecordSpecification(input);
        final Page<XtdProperty> page = service.findAll(specification);
        return Connection.of(page);
    }

    @SchemaMapping(typeName = "XtdProperty", field = "dimension")
    public Optional<XtdDimension> getDimension(XtdProperty property) {
        return service.getDimension(property);
    }

    @SchemaMapping(typeName = "XtdProperty", field = "possibleValues")
    public List<XtdValueList> getPossibleValues(XtdProperty property) {
        return service.getValueLists(property);
    }

    @SchemaMapping(typeName = "XtdProperty", field = "units")
    public List<XtdUnit> getUnits(XtdProperty property) {
        return service.getUnits(property);
    }

    @SchemaMapping(typeName = "XtdProperty", field = "connectedProperties")
    public List<XtdRelationshipToProperty> getConnectedProperties(XtdProperty property) {
        return service.getConnectedProperties(property);
    }

    @SchemaMapping(typeName = "XtdProperty", field = "connectingProperties")
    public List<XtdRelationshipToProperty> getConnectingProperties(XtdProperty property) {
        return service.getConnectingProperties(property);
    }

    @SchemaMapping(typeName = "XtdProperty", field = "symbols")
    public List<XtdSymbol> getSymbols(XtdProperty property) {
        return service.getSymbols(property);
    }

    @SchemaMapping(typeName = "XtdProperty", field = "boundaryValues")
    public List<XtdInterval> getBoundaryValues(XtdProperty property) {
        return service.getIntervals(property);
    }

    @SchemaMapping(typeName = "XtdProperty", field = "quantityKinds")
    public List<XtdQuantityKind> getQuantityKinds(XtdProperty property) {
        return service.getQuantityKinds(property);
    }

    @SchemaMapping(typeName = "XtdProperty", field = "subjects")
    public List<XtdSubject> getSubjects(XtdProperty property) {
        return service.getSubjects(property);
    }
}
