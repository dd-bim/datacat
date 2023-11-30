package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.CatalogRecord;
import de.bentrm.datacat.catalog.domain.XtdCollection;
import de.bentrm.datacat.catalog.domain.XtdRelCollects;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import de.bentrm.datacat.catalog.service.CatalogService;
import de.bentrm.datacat.catalog.service.CollectsRecordService;
import graphql.schema.DataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CollectsFetchers extends AbstractFetchers<XtdRelCollects> {

    private final DataFetcher<XtdCollection> relatingCollection;
    private final DataFetcher<List<XtdRoot>> relatedThings;

    public CollectsFetchers(CollectsRecordService entityService,
                            CatalogService catalogService) {
        super(entityService);

        this.relatingCollection = environment -> {
            final XtdRelCollects source = environment.getSource();
            final String id = source.getRelatingCollection().getId();
            final CatalogRecord collection = catalogService.getEntryById(id).orElseThrow();
            return (XtdCollection) collection;
        };

        this.relatedThings = environment -> {
            final XtdRelCollects source = environment.getSource();
            final List<String> relatedThingsIds = source.getRelatedThings().stream()
                    .map(CatalogRecord::getId)
                    .collect(Collectors.toList());
            return catalogService.getAllRootItemsById(relatedThingsIds);
        };
    }

    @Override
    public String getTypeName() {
        return "XtdRelCollects";
    }

    @Override
    public String getFetcherName() {
        return "getCollects";
    }

    @Override
    public String getListFetcherName() {
        return "findCollects";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        final Map<String, DataFetcher> fetchers = new HashMap<>(super.getAttributeFetchers());

        fetchers.put("relatingCollection", relatingCollection);
        fetchers.put("relatedThings", relatedThings);

        return fetchers;
    }
}
