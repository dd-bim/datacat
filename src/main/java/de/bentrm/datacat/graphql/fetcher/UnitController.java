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
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @BatchMapping(typeName = "XtdUnit", field = "properties")
    public Map<XtdUnit, List<XtdProperty>> getProperties(List<XtdUnit> units) {
        return units.stream()
                .filter(unit -> unit != null)  // Filter out null units
                .collect(Collectors.toMap(
                        unit -> unit,
                        unit -> {
                            List<XtdProperty> result = service.getProperties(unit);
                            return result != null ? result : new ArrayList<>();  // Handle null result
                        }
                ));                
    }

    @BatchMapping(typeName = "XtdUnit", field = "dimension")
    public Map<XtdUnit, Optional<XtdDimension>> getDimension(List<XtdUnit> units) {
        return units.stream()
                .filter(unit -> unit != null)  // Filter out null units
                .collect(Collectors.toMap(
                        unit -> unit,
                        unit -> {
                            Optional<XtdDimension> result = service.getDimension(unit);
                            return result != null ? result : Optional.empty();  // Handle null Optional
                        }
                ));                
    }

    @BatchMapping(typeName = "XtdUnit", field = "symbol")
    public Map<XtdUnit, Optional<XtdMultiLanguageText>> getSymbol(List<XtdUnit> units) {
        return units.stream()
                .filter(unit -> unit != null)  // Filter out null units
                .collect(Collectors.toMap(
                        unit -> unit,
                        unit -> {
                            Optional<XtdMultiLanguageText> result = service.getSymbol(unit);
                            return result != null ? result : Optional.empty();  // Handle null Optional
                        }
                ));                
    }

    @BatchMapping(typeName = "XtdUnit", field = "coefficient")
    public Map<XtdUnit, Optional<XtdRational>> getCoefficient(List<XtdUnit> units) {
        return units.stream()
                .filter(unit -> unit != null)  // Filter out null units
                .collect(Collectors.toMap(
                        unit -> unit,
                        unit -> {
                            Optional<XtdRational> result = service.getCoefficient(unit);
                            return result != null ? result : Optional.empty();  // Handle null Optional
                        }
                ));                
    }

    @BatchMapping(typeName = "XtdUnit", field = "offset")
    public Map<XtdUnit, Optional<XtdRational>> getOffset(List<XtdUnit> units) {
        return units.stream()
                .filter(unit -> unit != null)  // Filter out null units
                .collect(Collectors.toMap(
                        unit -> unit,
                        unit -> {
                            Optional<XtdRational> result = service.getOffset(unit);
                            return result != null ? result : Optional.empty();  // Handle null Optional
                        }
                ));                
    }

    @BatchMapping(typeName = "XtdUnit", field = "valueLists")
    public Map<XtdUnit, Optional<List<XtdValueList>>> getValueLists(List<XtdUnit> units) {
        return units.stream()
                .filter(unit -> unit != null)  // Filter out null units
                .collect(Collectors.toMap(
                        unit -> unit,
                        unit -> {
                            Optional<List<XtdValueList>> result = service.getValueLists(unit);
                            return result != null ? result : Optional.empty();  // Handle null Optional
                        }
                ));                
    }
}
