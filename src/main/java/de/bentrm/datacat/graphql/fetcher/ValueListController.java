package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdUnit;
import de.bentrm.datacat.catalog.domain.XtdLanguage;
import de.bentrm.datacat.catalog.domain.XtdOrderedValue;
import de.bentrm.datacat.catalog.domain.XtdValueList;
import de.bentrm.datacat.catalog.service.ValueListRecordService;
import de.bentrm.datacat.catalog.specification.CatalogRecordSpecification;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.FilterInput;
import de.bentrm.datacat.graphql.dto.SpecificationMapper;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class ValueListController {

    @Autowired
    private ValueListRecordService valueListRecordService;

    @Autowired
    private SpecificationMapper specificationMapper;

    @QueryMapping
    public Optional<XtdValueList> getValueList(@Argument String id) {
        return valueListRecordService.findById(id);
    }

    @QueryMapping
    public Connection<XtdValueList> findValueLists(@Argument FilterInput input) {
        if (input == null)
            input = new FilterInput();
        final CatalogRecordSpecification specification = specificationMapper.toCatalogRecordSpecification(input);
        final Page<XtdValueList> page = valueListRecordService.findAll(specification);
        return Connection.of(page);
    }

    @SchemaMapping(typeName = "XtdValueList", field = "values")
    public List<XtdOrderedValue> getOrderedValues(XtdValueList valueList) {
        return valueListRecordService.getOrderedValues(valueList);
    }

    @SchemaMapping(typeName = "XtdValueList", field = "properties")
    public List<XtdProperty> getProperties(XtdValueList valueList) {
        return valueListRecordService.getProperties(valueList);
    }

    @SchemaMapping(typeName = "XtdValueList", field = "unit")
    public Optional<XtdUnit> getUnit(XtdValueList valueList) {
        return valueListRecordService.getUnit(valueList);
    }

    @SchemaMapping(typeName = "XtdValueList", field = "language")
    public Optional<XtdLanguage> getLanguage(XtdValueList valueList) {
        return valueListRecordService.getLanguage(valueList);
    }
}
