package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdOrderedValue;
import de.bentrm.datacat.catalog.domain.XtdValue;
import de.bentrm.datacat.catalog.domain.XtdValueList;
import de.bentrm.datacat.catalog.service.OrderedValueRecordService;
import de.bentrm.datacat.catalog.specification.CatalogRecordSpecification;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.FilterInput;
import de.bentrm.datacat.graphql.dto.SpecificationMapper;
import lombok.extern.slf4j.Slf4j;

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

@Slf4j
@Controller
public class OrderedValueController {

    @Autowired
    private OrderedValueRecordService orderedValueRecordService;

    @Autowired
    private SpecificationMapper specificationMapper;

    @QueryMapping
    public Optional<XtdOrderedValue> getOrderedValue(@Argument String id) {
        return orderedValueRecordService.findByIdWithDirectRelations(id, XtdOrderedValue.class.getSimpleName());
    }

    @QueryMapping
    public Connection<XtdOrderedValue> findOrderedValues(@Argument FilterInput input) {
        if (input == null)
            input = new FilterInput();
        final CatalogRecordSpecification specification = specificationMapper.toCatalogRecordSpecification(input);
        final Page<XtdOrderedValue> page = orderedValueRecordService.findAll(specification);
        return Connection.of(page);
    }

    @BatchMapping(typeName = "XtdOrderedValue", field = "orderedValue")
    public Map<XtdOrderedValue, XtdValue> getOrderedValue(List<XtdOrderedValue> orderedValues) {
        return orderedValues.stream()
                .filter(orderedValue -> orderedValue != null)  // Filter out null orderedValues
                .collect(Collectors.toMap(
                        orderedValue -> orderedValue,
                        orderedValue -> {
                            // Verwende bereits geladene Daten wenn verfügbar
                            XtdValue result = orderedValue.getOrderedValue();
                            if (result == null) {
                                // Fallback: lade aus DB wenn nicht bereits geladen
                                result = orderedValueRecordService.getValue(orderedValue);
                            }
                            return result;  // Return as-is since it's not Optional or List
                        }
                ));                
    }

    @BatchMapping(typeName = "XtdOrderedValue", field = "valueLists")
    public Map<XtdOrderedValue, List<XtdValueList>> getValueLists(List<XtdOrderedValue> orderedValues) {
        return orderedValues.stream()
                .filter(orderedValue -> orderedValue != null)  // Filter out null orderedValues
                .collect(Collectors.toMap(
                        orderedValue -> orderedValue,
                        orderedValue -> {
                            // Verwende bereits geladene Daten wenn verfügbar
                            List<XtdValueList> result = new ArrayList<>(orderedValue.getValueLists());
                            if (result.isEmpty()) {
                                // Fallback: lade aus DB wenn nicht bereits geladen
                                result = orderedValueRecordService.getValueLists(orderedValue);
                            }
                            return result != null ? result : new ArrayList<>();  // Handle null result
                        }
                ));                
    }
}
