package de.bentrm.datacat.graphql.fetcher;

import graphql.schema.DataFetcher;

import java.util.Map;

public interface QueryFetchers {

    default Map<String, DataFetcher> getQueryFetchers() {
        return Map.of();
    }
}
