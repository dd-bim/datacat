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
import java.util.ArrayList;
import java.util.Set;

@Slf4j
@Controller
public class ValueListController {

    @Autowired
    private ValueListRecordService valueListRecordService;

    @Autowired
    private SpecificationMapper specificationMapper;

    @QueryMapping
    public Optional<XtdValueList> getValueList(@Argument String id) {
        long start = System.currentTimeMillis();
        Optional<XtdValueList> result = valueListRecordService.findByIdWithIncomingAndOutgoingRelations(id);
        long end = System.currentTimeMillis();
        log.info("Query executed in {} ms", end - start);
        return result;
    }

    @QueryMapping
    public Connection<XtdValueList> findValueLists(@Argument FilterInput input) {
        if (input == null)
            input = new FilterInput();
        final CatalogRecordSpecification specification = specificationMapper.toCatalogRecordSpecification(input);
        final Page<XtdValueList> page = valueListRecordService.findAll(specification);
        return Connection.of(page);
    }

    // Optimierte Schema-Mappings, die bereits geladene Daten verwenden
    @SchemaMapping(typeName = "XtdValueList", field = "values")
    public List<XtdOrderedValue> getOrderedValues(XtdValueList valueList) {
        return new ArrayList<>(valueList.getValues());
    }

    @SchemaMapping(typeName = "XtdValueList", field = "properties")
    public List<XtdProperty> getProperties(XtdValueList valueList) {
        Set<XtdProperty> loadedProperties = valueList.getProperties();
        if (loadedProperties != null && !loadedProperties.isEmpty()) {
            return new ArrayList<>(loadedProperties);
        } else {
            // Fallback: lade aus DB
            return valueListRecordService.getProperties(valueList);
        }
    }

    @SchemaMapping(typeName = "XtdValueList", field = "unit")
    public Optional<XtdUnit> getUnit(XtdValueList valueList) {
        return Optional.ofNullable(valueList.getUnit());
    }

    @SchemaMapping(typeName = "XtdValueList", field = "language")
    public Optional<XtdLanguage> getLanguage(XtdValueList valueList) {
        return Optional.ofNullable(valueList.getLanguage());
    }
}
