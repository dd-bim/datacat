package de.bentrm.datacat.graphql.fetcher.delegate;

import graphql.schema.DataFetcher;

import java.util.Map;

/**
 * Marker interface to designate fetching delegates, classes that implement
 * a data fetcher that a concrete implementation can forward to.
 */
public interface FetchingDelegate {
    Map<String, DataFetcher> getFetchers();
}
