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
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

@Slf4j
@Controller
public class PropertyController {

    @Autowired
    private PropertyRecordService service;

    @Autowired
    private SpecificationMapper specificationMapper;

    @QueryMapping
    public Optional<XtdProperty> getProperty(@Argument String id) {
        long start = System.currentTimeMillis();
        Optional<XtdProperty> result = service.findByIdWithIncomingAndOutgoingRelations(id);
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

    // Optimierte Schema-Mappings, die bereits geladene Daten verwenden
    @SchemaMapping(typeName = "XtdProperty", field = "dimension")
    public Optional<XtdDimension> getDimension(XtdProperty property) {
        XtdDimension loadedDimension = property.getDimension();
        if (loadedDimension != null) {
            return Optional.of(loadedDimension);
        } else {
            // Fallback: lade aus DB
            return service.getDimension(property);
        }
    }

    @SchemaMapping(typeName = "XtdProperty", field = "possibleValues")
    public List<XtdValueList> getPossibleValues(XtdProperty property) {
        Set<XtdValueList> loadedValueLists = property.getPossibleValues();
        if (loadedValueLists != null && !loadedValueLists.isEmpty()) {
            return new ArrayList<>(loadedValueLists);
        } else {
            // Fallback: lade aus DB
            return service.getValueLists(property);
        }
    }

    @SchemaMapping(typeName = "XtdProperty", field = "units")
    public List<XtdUnit> getUnits(XtdProperty property) {
        Set<XtdUnit> loadedUnits = property.getUnits();
        if (loadedUnits != null && !loadedUnits.isEmpty()) {
            return new ArrayList<>(loadedUnits);
        } else {
            // Fallback: lade aus DB
            return service.getUnits(property);
        }
    }

    @SchemaMapping(typeName = "XtdProperty", field = "connectedProperties")
    public List<XtdRelationshipToProperty> getConnectedProperties(XtdProperty property) {
        Set<XtdRelationshipToProperty> loadedConnectedProperties = property.getConnectedProperties();
        if (loadedConnectedProperties != null && !loadedConnectedProperties.isEmpty()) {
            return new ArrayList<>(loadedConnectedProperties);
        } else {
            // Fallback: lade aus DB
            return service.getConnectedProperties(property);
        }
    }

    @SchemaMapping(typeName = "XtdProperty", field = "connectingProperties")
    public List<XtdRelationshipToProperty> getConnectingProperties(XtdProperty property) {
        // Note: connectingProperties werden über inverse Beziehung geladen, 
        // diese sind möglicherweise nicht direkt in der Property verfügbar
        // Fallback zur DB-Abfrage
        return service.getConnectingProperties(property);
    }

    @SchemaMapping(typeName = "XtdProperty", field = "symbols")
    public List<XtdSymbol> getSymbols(XtdProperty property) {
        Set<XtdSymbol> loadedSymbols = property.getSymbols();
        if (loadedSymbols != null && !loadedSymbols.isEmpty()) {
            return new ArrayList<>(loadedSymbols);
        } else {
            // Fallback: lade aus DB
            return service.getSymbols(property);
        }
    }

    @SchemaMapping(typeName = "XtdProperty", field = "boundaryValues")
    public List<XtdInterval> getBoundaryValues(XtdProperty property) {
        Set<XtdInterval> loadedBoundaryValues = property.getBoundaryValues();
        if (loadedBoundaryValues != null && !loadedBoundaryValues.isEmpty()) {
            return new ArrayList<>(loadedBoundaryValues);
        } else {
            // Fallback: lade aus DB
            return service.getIntervals(property);
        }
    }

    @SchemaMapping(typeName = "XtdProperty", field = "quantityKinds")
    public List<XtdQuantityKind> getQuantityKinds(XtdProperty property) {
        Set<XtdQuantityKind> loadedQuantityKinds = property.getQuantityKinds();
        if (loadedQuantityKinds != null && !loadedQuantityKinds.isEmpty()) {
            return new ArrayList<>(loadedQuantityKinds);
        } else {
            // Fallback: lade aus DB
            return service.getQuantityKinds(property);
        }
    }

    @SchemaMapping(typeName = "XtdProperty", field = "subjects")
    public List<XtdSubject> getSubjects(XtdProperty property) {
        // Note: subjects werden über inverse Beziehung geladen,
        // diese sind möglicherweise nicht direkt in der Property verfügbar
        // Fallback zur DB-Abfrage
        return service.getSubjects(property);
    }
}
