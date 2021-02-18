package de.bentrm.datacat.graphql.fetcher;

import graphql.schema.DataFetcher;

import java.util.Map;

/**
 * Marker interface implemented by classes that map mutation fetchers of the API.
 */
public interface MutationFetchers {

    String INPUT_ARGUMENT = "input";

    /**
     * @return a map of mutation fetchers.
     */
    Map<String, DataFetcher> getMutationFetchers();
}
