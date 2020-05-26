package de.bentrm.datacat.graphql.fetcher;

import graphql.schema.DataFetcher;

import java.util.Map;

public interface ObjectDataFetcherProvider {

    Map<String, DataFetcher> getObjectDataFetchers();
}
