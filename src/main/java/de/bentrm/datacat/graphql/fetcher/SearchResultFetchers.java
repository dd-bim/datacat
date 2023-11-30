package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.CatalogRecord;
import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SearchResultFetchers implements AttributeFetchers {

    @Override
    public String getTypeName() {
        return "SearchResult";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        return Map.of(
                "recordType", environment -> {
                    final CatalogRecord source = environment.getSource();
                    return CatalogRecordType.getByDomainClass(source);
                },
                "name", new NameFetcher(),
                "description", new DescriptionFetcher(),
                "comment", new CommentFetcher()
        );
    }
}
