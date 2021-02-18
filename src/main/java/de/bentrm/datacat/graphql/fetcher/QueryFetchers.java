package de.bentrm.datacat.graphql.fetcher;

import graphql.schema.DataFetcher;

import java.util.Map;

/**
 * Marker interface implemented by classes that map query fetchers of the API.
 */
public interface QueryFetchers {

    /**
     * @return A map of query fetchers.
     */
    default Map<String, DataFetcher> getQueryFetchers() {
        return Map.of();
    }
}
