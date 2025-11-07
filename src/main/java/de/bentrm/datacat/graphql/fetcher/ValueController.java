package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdOrderedValue;
import de.bentrm.datacat.catalog.domain.XtdValue;
import de.bentrm.datacat.catalog.service.ValueRecordService;
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

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class ValueController {

    @Autowired
    private ValueRecordService valueRecordService;

    @Autowired
    private SpecificationMapper specificationMapper;

    @QueryMapping
    public Optional<XtdValue> getValue(@Argument String id) {
        return valueRecordService.findByIdWithDirectRelations(id, XtdValue.class.getSimpleName());
    }

    @QueryMapping
    public Connection<XtdValue> findValues(@Argument FilterInput input) {
        if (input == null)
            input = new FilterInput();
        final CatalogRecordSpecification specification = specificationMapper.toCatalogRecordSpecification(input);
        final Page<XtdValue> page = valueRecordService.findAll(specification);
        return Connection.of(page);
    }

    @BatchMapping(typeName = "XtdValue", field = "orderedValues")
    public Map<XtdValue, Optional<List<XtdOrderedValue>>> getOrderedValues(List<XtdValue> values) {
        return values.stream()
                .filter(value -> value != null)  // Filter out null values
                .collect(Collectors.toMap(
                        value -> value,
                        value -> {
                            Optional<List<XtdOrderedValue>> result = valueRecordService.getOrderedValues(value);
                            return result != null ? result : Optional.empty();  // Handle null Optional
                        }
                ));                
    }
}
