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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class ValueListController {

    @Autowired
    private ValueListRecordService valueListRecordService;

    @Autowired
    private SpecificationMapper specificationMapper;

    @QueryMapping
    public Optional<XtdValueList> getValueList(@Argument String id) {
        // Lade ValueList OHNE jegliche Relationen - alle Felder werden über separate Resolver geladen
        long start = System.currentTimeMillis();
        Optional<XtdValueList> result = valueListRecordService.findByIdWithoutRelations(id);
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

    @SchemaMapping(typeName = "XtdValueList", field = "values")
    public Connection<XtdOrderedValue> getOrderedValues(XtdValueList valueList,
                                            @Argument Integer pageSize, 
                                            @Argument Integer pageNumber) {
        if (pageSize == null) pageSize = 20;
        if (pageNumber == null) pageNumber = 0;
        
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<XtdOrderedValue> page = valueListRecordService.getOrderedValues(valueList, pageable);
        return Connection.of(page);
    }

    @BatchMapping(typeName = "XtdValueList", field = "properties")
    public Map<XtdValueList, List<XtdProperty>> getProperties(List<XtdValueList> valueLists) {
        return valueLists.stream().filter(valueList -> valueList != null) // Filter out null valueLists
                .collect(Collectors.toMap(valueList -> valueList, valueList -> {
                    Set<XtdProperty> loadedProperties = valueList.getProperties();
                    if (loadedProperties != null && !loadedProperties.isEmpty()) {
                        return new ArrayList<>(loadedProperties);
                    } else {
                        // Fallback: lade aus DB
                        List<XtdProperty> result = valueListRecordService.getProperties(valueList);
                        return result != null ? result : new ArrayList<>(); // Handle null result
                    }
                }));
    }

    @BatchMapping(typeName = "XtdValueList", field = "unit")
    public Map<XtdValueList, Optional<XtdUnit>> getUnit(List<XtdValueList> valueLists) {
        return valueLists.stream().filter(valueList -> valueList != null) // Filter out null valueLists
                .collect(Collectors.toMap(valueList -> valueList, valueList -> {
                    Optional<XtdUnit> result = Optional.ofNullable(valueList.getUnit());
                    return result != null ? result : Optional.empty(); // Handle null Optional
                }));
    }

    @BatchMapping(typeName = "XtdValueList", field = "language")
    public Map<XtdValueList, Optional<XtdLanguage>> getLanguage(List<XtdValueList> valueLists) {
        return valueLists.stream().filter(valueList -> valueList != null) // Filter out null valueLists
                .collect(Collectors.toMap(valueList -> valueList, valueList -> {
                    Optional<XtdLanguage> result = Optional.ofNullable(valueList.getLanguage());
                    return result != null ? result : Optional.empty(); // Handle null Optional
                }));
    }
}
