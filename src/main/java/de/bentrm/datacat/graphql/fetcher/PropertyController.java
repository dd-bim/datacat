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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class PropertyController {

    @Autowired
    private PropertyRecordService service;

    @Autowired
    private SpecificationMapper specificationMapper;

    @QueryMapping
    public Optional<XtdProperty> getProperty(@Argument String id) {
        // Lade Property OHNE jegliche Relationen - alle Felder werden über separate Resolver geladen
        long start = System.currentTimeMillis();
        Optional<XtdProperty> result = service.findById(id);
        long end = System.currentTimeMillis();
        log.info("getProperty executed in {} ms", end - start);
        return result;
    }

    @QueryMapping
    public Connection<XtdProperty> findProperties(@Argument FilterInput input) {
        if (input == null)
            input = new FilterInput();
        final CatalogRecordSpecification specification = specificationMapper.toCatalogRecordSpecification(input);
        final Page<XtdProperty> page = service.findAll(specification);
        return Connection.of(page);
    }

    // Optimierte Batch-Mappings, die bereits geladene Daten verwenden
    @BatchMapping(typeName = "XtdProperty", field = "dimension")
    public Map<XtdProperty, Optional<XtdDimension>> getDimension(List<XtdProperty> properties) {
        return properties.stream()
                .filter(property -> property != null)  // Filter out null properties
                .collect(Collectors.toMap(
                        property -> property,
                        property -> {
                            XtdDimension loadedDimension = property.getDimension();
                            if (loadedDimension != null) {
                                return Optional.of(loadedDimension);
                            } else {
                                // Fallback: lade aus DB
                                Optional<XtdDimension> result = service.getDimension(property);
                                return result != null ? result : Optional.empty();  // Handle null Optional
                            }
                        }
                ));                
    }

    @BatchMapping(typeName = "XtdProperty", field = "possibleValues")
    public Map<XtdProperty, List<XtdValueList>> getPossibleValues(List<XtdProperty> properties) {
        return properties.stream()
                .filter(property -> property != null)  // Filter out null properties
                .collect(Collectors.toMap(
                        property -> property,
                        property -> {
                            Set<XtdValueList> loadedValueLists = property.getPossibleValues();
                            if (loadedValueLists != null && !loadedValueLists.isEmpty()) {
                                return new ArrayList<>(loadedValueLists);
                            } else {
                                // Fallback: lade aus DB
                                List<XtdValueList> result = service.getValueLists(property);
                                return result != null ? result : new ArrayList<>();  // Handle null result
                            }
                        }
                ));                
    }

    @BatchMapping(typeName = "XtdProperty", field = "units")
    public Map<XtdProperty, List<XtdUnit>> getUnits(List<XtdProperty> properties) {
        return properties.stream()
                .filter(property -> property != null)  // Filter out null properties
                .collect(Collectors.toMap(
                        property -> property,
                        property -> {
                            Set<XtdUnit> loadedUnits = property.getUnits();
                            if (loadedUnits != null && !loadedUnits.isEmpty()) {
                                return new ArrayList<>(loadedUnits);
                            } else {
                                // Fallback: lade aus DB
                                List<XtdUnit> result = service.getUnits(property);
                                return result != null ? result : new ArrayList<>();  // Handle null result
                            }
                        }
                ));                
    }

    @BatchMapping(typeName = "XtdProperty", field = "connectedProperties")
    public Map<XtdProperty, List<XtdRelationshipToProperty>> getConnectedProperties(List<XtdProperty> properties) {
        return properties.stream()
                .filter(property -> property != null)  // Filter out null properties
                .collect(Collectors.toMap(
                        property -> property,
                        property -> {
                            Set<XtdRelationshipToProperty> loadedConnectedProperties = property.getConnectedProperties();
                            if (loadedConnectedProperties != null && !loadedConnectedProperties.isEmpty()) {
                                return new ArrayList<>(loadedConnectedProperties);
                            } else {
                                // Fallback: lade aus DB
                                List<XtdRelationshipToProperty> result = service.getConnectedProperties(property);
                                return result != null ? result : new ArrayList<>();  // Handle null result
                            }
                        }
                ));                
    }

    @BatchMapping(typeName = "XtdProperty", field = "connectingProperties")
    public Map<XtdProperty, List<XtdRelationshipToProperty>> getConnectingProperties(List<XtdProperty> properties) {
        return properties.stream()
                .filter(property -> property != null)  // Filter out null properties
                .collect(Collectors.toMap(
                        property -> property,
                        property -> {
                            // Note: connectingProperties werden über inverse Beziehung geladen, 
                            // diese sind möglicherweise nicht direkt in der Property verfügbar
                            // Fallback zur DB-Abfrage
                            List<XtdRelationshipToProperty> result = service.getConnectingProperties(property);
                            return result != null ? result : new ArrayList<>();  // Handle null result
                        }
                ));                
    }

    @BatchMapping(typeName = "XtdProperty", field = "symbols")
    public Map<XtdProperty, List<XtdSymbol>> getSymbols(List<XtdProperty> properties) {
        return properties.stream()
                .filter(property -> property != null)  // Filter out null properties
                .collect(Collectors.toMap(
                        property -> property,
                        property -> {
                            Set<XtdSymbol> loadedSymbols = property.getSymbols();
                            if (loadedSymbols != null && !loadedSymbols.isEmpty()) {
                                return new ArrayList<>(loadedSymbols);
                            } else {
                                // Fallback: lade aus DB
                                List<XtdSymbol> result = service.getSymbols(property);
                                return result != null ? result : new ArrayList<>();  // Handle null result
                            }
                        }
                ));                
    }

    @BatchMapping(typeName = "XtdProperty", field = "boundaryValues")
    public Map<XtdProperty, List<XtdInterval>> getBoundaryValues(List<XtdProperty> properties) {
        return properties.stream()
                .filter(property -> property != null)  // Filter out null properties
                .collect(Collectors.toMap(
                        property -> property,
                        property -> {
                            Set<XtdInterval> loadedBoundaryValues = property.getBoundaryValues();
                            if (loadedBoundaryValues != null && !loadedBoundaryValues.isEmpty()) {
                                return new ArrayList<>(loadedBoundaryValues);
                            } else {
                                // Fallback: lade aus DB
                                List<XtdInterval> result = service.getIntervals(property);
                                return result != null ? result : new ArrayList<>();  // Handle null result
                            }
                        }
                ));                
    }

    @BatchMapping(typeName = "XtdProperty", field = "quantityKinds")
    public Map<XtdProperty, List<XtdQuantityKind>> getQuantityKinds(List<XtdProperty> properties) {
        return properties.stream()
                .filter(property -> property != null)  // Filter out null properties
                .collect(Collectors.toMap(
                        property -> property,
                        property -> {
                            Set<XtdQuantityKind> loadedQuantityKinds = property.getQuantityKinds();
                            if (loadedQuantityKinds != null && !loadedQuantityKinds.isEmpty()) {
                                return new ArrayList<>(loadedQuantityKinds);
                            } else {
                                // Fallback: lade aus DB
                                List<XtdQuantityKind> result = service.getQuantityKinds(property);
                                return result != null ? result : new ArrayList<>();  // Handle null result
                            }
                        }
                ));                
    }

    @BatchMapping(typeName = "XtdProperty", field = "subjects")
    public Map<XtdProperty, List<XtdSubject>> getSubjects(List<XtdProperty> properties) {
        return properties.stream()
                .filter(property -> property != null)  // Filter out null properties
                .collect(Collectors.toMap(
                        property -> property,
                        property -> {
                            // Note: subjects werden über inverse Beziehung geladen,
                            // diese sind möglicherweise nicht direkt in der Property verfügbar
                            // Fallback zur DB-Abfrage
                            List<XtdSubject> result = service.getSubjects(property);
                            return result != null ? result : new ArrayList<>();  // Handle null result
                        }
                ));                
    }
}
