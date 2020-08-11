package de.bentrm.datacat.graphql.fetcher;

import graphql.schema.DataFetcher;

import java.util.Map;

public interface MutationFetchers {

    Map<String, DataFetcher> getMutationFetchers();
}
