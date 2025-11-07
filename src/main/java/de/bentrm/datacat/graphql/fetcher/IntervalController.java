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
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @BatchMapping(typeName = "XtdInterval", field = "minimum")
    public Map<XtdInterval, Optional<XtdValueList>> getMinimum(List<XtdInterval> intervals) {
        return intervals.stream()
                .filter(interval -> interval != null)  // Filter out null intervals
                .collect(Collectors.toMap(
                        interval -> interval,
                        interval -> {
                            Optional<XtdValueList> result = service.getMinimum(interval);
                            return result != null ? result : Optional.empty();  // Handle null Optional
                        }
                ));                
    }

    @BatchMapping(typeName = "XtdInterval", field = "maximum")
    public Map<XtdInterval, Optional<XtdValueList>> getMaximum(List<XtdInterval> intervals) {
        return intervals.stream()
                .filter(interval -> interval != null)  // Filter out null intervals
                .collect(Collectors.toMap(
                        interval -> interval,
                        interval -> {
                            Optional<XtdValueList> result = service.getMaximum(interval);
                            return result != null ? result : Optional.empty();  // Handle null Optional
                        }
                ));                
    }
}
