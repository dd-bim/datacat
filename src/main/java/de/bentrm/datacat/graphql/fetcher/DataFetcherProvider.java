package de.bentrm.datacat.graphql.fetcher;

import graphql.schema.DataFetcher;

import java.util.Map;

public interface DataFetcherProvider {

    default Map<String, DataFetcher> getDataFetchers() {
        return Map.ofEntries();
    };
}
