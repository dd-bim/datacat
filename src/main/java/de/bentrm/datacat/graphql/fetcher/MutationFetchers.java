package de.bentrm.datacat.graphql.fetcher;

import graphql.schema.DataFetcher;

import java.util.Map;

public interface MutationFetchers {

    String INPUT_ARGUMENT = "input";

    Map<String, DataFetcher> getMutationFetchers();
}
