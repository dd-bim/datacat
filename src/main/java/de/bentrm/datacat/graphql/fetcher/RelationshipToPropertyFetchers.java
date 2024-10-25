package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.CatalogRecord;
import de.bentrm.datacat.catalog.domain.XtdRelationshipToProperty;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.service.CatalogService;
import de.bentrm.datacat.catalog.service.RelationshipToPropertyRecordService;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RelationshipToPropertyFetchers extends AbstractFetchers<XtdRelationshipToProperty> {

    private final DataFetcher<XtdProperty> connectingProperty;
    private final DataFetcher<List<XtdRoot>> targetProperties;

    public RelationshipToPropertyFetchers(RelationshipToPropertyRecordService entityService,
                            CatalogService catalogService) {
        super(entityService);

        this.connectingProperty = environment -> {
            final XtdRelationshipToProperty source = environment.getSource();
            final String id = source.getConnectingProperty().getId();
            final CatalogRecord collection = catalogService.getEntryById(id).orElseThrow();
            return (XtdProperty) collection;
        };

        this.targetProperties = environment -> {
            final XtdRelationshipToProperty source = environment.getSource();
            final List<String> targetProperties = source.getTargetProperties().stream()
                    .map(CatalogRecord::getId)
                    .collect(Collectors.toList());
            return catalogService.getAllRootItemsById(targetProperties);
        };
    }

    @Override
    public String getTypeName() {
        return "XtdRelationshipToProperty";
    }

    @Override
    public String getFetcherName() {
        return "getRelationshipToProperty";
    }

    @Override
    public String getListFetcherName() {
        return "findRelationshipToPropertys";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        final Map<String, DataFetcher> fetchers = new HashMap<>(super.getAttributeFetchers());

        fetchers.put("connectingProperty", connectingProperty);
        fetchers.put("targetProperties", targetProperties);

        return fetchers;
    }
}
