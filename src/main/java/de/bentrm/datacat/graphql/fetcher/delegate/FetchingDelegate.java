package de.bentrm.datacat.graphql.fetcher.delegate;

import graphql.schema.DataFetcher;

import java.util.Map;

public interface FetchingDelegate {
    Map<String, DataFetcher> getFetchers();
}
