package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.*;
import de.bentrm.datacat.catalog.service.value.CatalogEntryProperties;
import de.bentrm.datacat.catalog.service.value.ValueMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Slf4j
@Component
public class CatalogEntryFactory {

    private final ValueMapper mapper;

    public CatalogEntryFactory(ValueMapper mapper) {
        this.mapper = mapper;
    }

    public CatalogItem create(CatalogEntryType type, CatalogEntryProperties properties) {
        Assert.notNull(type, "type may not be null");
        Assert.notNull(properties, "properties may not be null");

        log.trace("Creating new catalog entry of type {}: {}", type, properties);

        CatalogItem catalogEntry = switch (type) {
            case Activity -> new XtdActivity();
            case Actor -> new XtdActor();
            case Bag -> new XtdBag();
            case Classification -> new XtdClassification();
            case ExternalDocument -> new XtdExternalDocument();
            case Measure -> new XtdMeasureWithUnit();
            case Nest -> new XtdNest();
            case Property -> new XtdProperty();
            case Subject -> new XtdSubject();
            case Unit -> new XtdUnit();
            case Value -> new XtdValue();
        };

        log.trace("Initialized new object: {}", catalogEntry);

        mapper.setProperties(properties, catalogEntry);

        log.trace("Initialized catalog entry properties: {}", catalogEntry);

        return catalogEntry;
    }
}
