package de.bentrm.datacat.graphql.fetcher;

import graphql.schema.DataFetcher;

import java.util.Map;

@FunctionalInterface
public interface MutationDataFetcherProvider {

    Map<String, DataFetcher> getMutationDataFetchers();
}
