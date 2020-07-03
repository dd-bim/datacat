package de.bentrm.datacat.graphql.fetcher;

import graphql.schema.DataFetcher;

import java.util.Map;

public interface PropertyProvider {
    String getType();
    Map<String, DataFetcher> getPropertyFetchers();
}
