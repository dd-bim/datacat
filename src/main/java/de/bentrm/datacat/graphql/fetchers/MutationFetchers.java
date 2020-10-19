package de.bentrm.datacat.graphql.fetchers;

import graphql.schema.DataFetcher;

import java.util.Map;

public interface MutationFetchers {

    Map<String, DataFetcher> getMutationFetchers();
}
