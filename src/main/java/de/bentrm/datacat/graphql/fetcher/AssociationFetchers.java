package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.Association;
import de.bentrm.datacat.catalog.domain.CatalogItem;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import de.bentrm.datacat.catalog.service.CatalogService;
import de.bentrm.datacat.catalog.service.QueryService;
import graphql.schema.DataFetcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AssociationFetchers<T extends Association> extends AbstractFetchers<T> {

    private final DataFetcher<XtdRoot> relatingThing;
    private final DataFetcher<List<XtdRoot>> relatedThings;

    public AssociationFetchers(QueryService<T> queryService, CatalogService catalogService) {
        super(queryService);

        this.relatingThing = environment -> {
            final Association source = environment.getSource();
            final String id = source.getRelatingThing().getId();
            return catalogService.getRootItem(id).orElseThrow();
        };

        this.relatedThings = environment -> {
            final Association source = environment.getSource();
            final List<String> relatedValuesId = source.getRelatedThings().stream()
                    .map(CatalogItem::getId)
                    .collect(Collectors.toList());
            return catalogService.getAllRootItemsById(relatedValuesId);
        };
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        final Map<String, DataFetcher> fetchers = new HashMap<>(super.getAttributeFetchers());
        fetchers.put("relatingThing", relatingThing);
        fetchers.put("relatedThings", relatedThings);
        return fetchers;
    }
}
