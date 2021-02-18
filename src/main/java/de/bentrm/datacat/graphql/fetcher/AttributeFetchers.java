package de.bentrm.datacat.graphql.fetcher;

import graphql.schema.DataFetcher;

import java.util.Map;

/**
 * Marker interface of classes that implement attribute fetchers of the API types.
 */
public interface AttributeFetchers {

    String getTypeName();

    /**
     * @return A map of data fetchers according to the API description of the current type.
     */
    Map<String, DataFetcher> getAttributeFetchers();
}
