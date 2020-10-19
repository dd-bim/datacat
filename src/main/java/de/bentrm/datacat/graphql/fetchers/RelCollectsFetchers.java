package de.bentrm.datacat.graphql.fetchers;

import de.bentrm.datacat.catalog.domain.CatalogItem;
import de.bentrm.datacat.catalog.domain.XtdCollection;
import de.bentrm.datacat.catalog.domain.XtdRelCollects;
import de.bentrm.datacat.catalog.service.CollectsService;
import graphql.schema.DataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RelCollectsFetchers
        extends AbstractEntityFetchers<XtdRelCollects, CollectsService> {

    public RelCollectsFetchers(CollectsService entityService) {
        super(entityService);
    }

    @Override
    public String getTypeName() {
        return "XtdRelCollects";
    }

    @Override
    public String getFetcherName() {
        return "collectsRelation";
    }

    @Override
    public String getListFetcherName() {
        return "collectsRelations";
    }

    @Override
    public String getMutationNameSuffix() {
        return "CollectsRelation";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        final Map<String, DataFetcher> fetchers = super.getAttributeFetchers();

        fetchers.put("relatingCollection", relatingCollection());
        fetchers.put("relatedThings", relatedThings());

        return fetchers;
    }

    private DataFetcher<XtdCollection> relatingCollection() {
        return environment -> {
            final XtdRelCollects source = environment.getSource();
            final String id = source.getRelatingCollection().getId();
            final CatalogItem collection = catalogService.getEntryById(id).orElseThrow();
            return (XtdCollection) collection;
        };
    }

    private DataFetcher<List<CatalogItem>> relatedThings() {
        return environment -> {
            final XtdRelCollects source = environment.getSource();
            final List<String> relatedThingsIds = source.getRelatedThings().stream()
                    .map(CatalogItem::getId)
                    .collect(Collectors.toList());
            return catalogService.getAllEntriesById(relatedThingsIds);
        };
    }
}
