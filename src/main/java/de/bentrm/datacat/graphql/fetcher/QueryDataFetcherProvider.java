package de.bentrm.datacat.graphql.fetcher;

import graphql.schema.DataFetcher;

import java.util.Map;

@FunctionalInterface
public interface QueryDataFetcherProvider {

    Map<String, DataFetcher> getQueryDataFetchers();
}
